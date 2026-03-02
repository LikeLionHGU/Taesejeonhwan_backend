package org.example.taesejeanhwan_backend.service;

import org.example.taesejeanhwan_backend.domain.*;
import org.example.taesejeanhwan_backend.dto.user.response.UserResponseTop5Genre;
import org.example.taesejeanhwan_backend.dto.user.request.UserRequestUpdateTop5Genre;
import org.example.taesejeanhwan_backend.dto.user.GenreStatDto;
import org.example.taesejeanhwan_backend.dto.user.request.*;
import org.example.taesejeanhwan_backend.dto.user.response.*;
import org.example.taesejeanhwan_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final ContentGenreRepository contentGenreRepository;
    private final UserGenreRepository userGenreRepository;
    private final GenreRepository genreRepository;
    private final ProfileImgRepository profileImgRepository;
    private final FollowRepository followRepository;
    private final ReviewRepository reviewRepository;
    private final UserContentRepository userContentsRepository;

    public UserResponseResult setProfileImage(UserRequestSetImage request) {

        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileImg profile = profileImgRepository.findByImgUrl(request.getProfile_img());

        if (profile == null) {
            throw new IllegalArgumentException("Profile image not found");
        }

        user.setProfileImg(profile);

        return UserResponseResult.builder()
                .result("success")
                .build();
    }

    public UserResponseResult setNickname(UserRequestSetNickname request) {

        if (request.getNickname() == null) {
            throw new IllegalArgumentException("nickname cannot be null");
        }
        if (request.getUser_id() == null) {
            throw new IllegalArgumentException("user_id cannot be null");
        }

        User user = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setNickname(request.getNickname());

        return UserResponseResult.builder()
                .result("success")
                .build();
    }



    public UserResponseTop5Genre setPreference(UserRequestSetPreference userRequestSetPreference) {
        try {
            // content_id와 rating map하기
            Map<Long, Float> ratingMap = userRequestSetPreference.getUser_contents()
                    .stream()
                    .collect(Collectors.toMap(
                            UserRequestSetPreferenceContent::getContent_id,
                            UserRequestSetPreferenceContent::getRating
                    ));

            List<Long> contentIds = new ArrayList<>(ratingMap.keySet());
            LocalDate localDate = LocalDate.now();

            User reviewUser = userRepository.findById(userRequestSetPreference.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Content> contents = contentRepository.findAllById(contentIds);
            Map<Long, Content> contentMap = contents.stream()
                    .collect(Collectors.toMap(Content::getId, c -> c));

            // 리뷰 저장
            for (Long contentId : contentIds) {
                Float contentRating = ratingMap.get(contentId);
                Content content = contentMap.get(contentId);

                reviewRepository.save(
                        Review.builder()
                                .rating(contentRating)
                                .comment(null)
                                .createTime(localDate)
                                .user(reviewUser)
                                .content(content)
                                .build()
                );

                userContentsRepository.save(
                        UserContent.builder()
                                .user(reviewUser)
                                .content(content)
                                .build()
                );
            }

            // 장르 통계 계산
            List<ContentGenre> contentGenres = contentGenreRepository.findAllByContent_IdIn(contentIds);

            Map<Long, GenreStatDto> genreStatMap = new HashMap<>();
            for (ContentGenre cg : contentGenres) {
                Long genreId = cg.getGenre().getId();
                Long contentId = cg.getContent().getId();
                double rating = ratingMap.get(contentId);

                genreStatMap.merge(
                        genreId,
                        new GenreStatDto(genreId, 1, rating, contentId),
                        (oldStat, newStat) -> new GenreStatDto(
                                genreId,
                                oldStat.getCount() + 1,
                                oldStat.getRatingSum() + rating,
                                Math.min(oldStat.getMinContentId(), contentId)
                        )
                );
            }

            // 상위 5개 장르 계산
            List<GenreStatDto> top5Genres = genreStatMap.values().stream()
                    .sorted(
                            Comparator
                                    .comparingLong(GenreStatDto::getCount).reversed()
                                    .thenComparingDouble(GenreStatDto::getAvgRating).reversed()
                                    .thenComparingLong(GenreStatDto::getMinContentId)
                    )
                    .limit(5)
                    .toList();

            // DB에 top5 저장
            userGenreRepository.deleteByUser_Id(userRequestSetPreference.getUser_id());

            List<UserGenre> userGenres = top5Genres.stream()
                    .map(stat -> UserGenre.builder()
                            .user(userRepository.getReferenceById(userRequestSetPreference.getUser_id()))
                            .genre(genreRepository.getReferenceById(stat.getGenreId()))
                            .build()
                    )
                    .toList();

            userGenreRepository.saveAll(userGenres);


            List<UserResponseTop5Genre.TopGenreDto> responseTop5 = top5Genres.stream()
                    .map(stat -> {
                        Genre g = genreRepository.getReferenceById(stat.getGenreId());
                        return UserResponseTop5Genre.TopGenreDto.builder()
                                .genre_id(g.getId())
                                .genre_name(g.getGenreName())
                                .build();
                    })
                    .toList();

            return UserResponseTop5Genre.builder()
                    .result("success")
                    .top5_genres(responseTop5)
                    .build();

        } catch (Exception e) {
            return UserResponseTop5Genre.builder()
                    .result("fail")
                    .top5_genres(Collections.emptyList())
                    .build();
        }
    }

    public UserResponseCheckNickname checkNickname(String nickname) {
        if(nickname.isEmpty()) {
            throw new IllegalArgumentException("nickname cannot be empty");
        }
        if(userRepository.existsByNickname(nickname)) {
            return UserResponseCheckNickname.builder()
                    .available(false)
                    .build();
        }
        return UserResponseCheckNickname.builder()
                .available(true)
                .build();
    }

    public List<UserResponseProfileImage> getAllProfileImages() {
        List<ProfileImg> profileImgs = profileImgRepository.findAll();
        return profileImgs.stream()
                .map(UserResponseProfileImage::from)
                .toList();
    }

    public UserResponseProfile getProfile(Long loginUserId, Long targetUserId) {
        if (targetUserId == null) {
            throw new IllegalArgumentException("user_id cannot be null");
        }

        try {
            User target = userRepository.findById(targetUserId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            int followerCount = followRepository.findAllByAnotherUser_Id(targetUserId).size();
            int followingCount = followRepository.findAllByUser_id(targetUserId).size();

            List<UserResponseProfileGenre> table = userGenreRepository.findByUser_Id(targetUserId).stream()
                    .map(UserResponseProfileGenre::from)
                    .toList();

            // ✅ is_following 계산
            boolean isFollowing = false;
            if (loginUserId != null && !loginUserId.equals(targetUserId)) {
                User me = userRepository.findById(loginUserId)
                        .orElseThrow(() -> new RuntimeException("Login user not found"));

                isFollowing = followRepository.existsByUserAndAnotherUser(me, target);
            }

            // ✅ profile_img null-safe
            String profileImgUrl = (target.getProfileImg() != null) ? target.getProfileImg().getImgUrl() : null;

            return UserResponseProfile.builder()
                    .result("success")
                    .user_id(target.getId())          // ✅ 포함
                    .is_following(isFollowing)        // ✅ 포함
                    .nickname(target.getNickname())
                    .profile_img(profileImgUrl)
                    .stats(UserResponseProfileDataFollow.builder()
                            .follower_count(followerCount)
                            .following_count(followingCount)
                            .build()
                    )
                    .table(table)
                    .build();

        } catch (Exception e) {
            return UserResponseProfile.builder()
                    .result("fail")
                    .build();
        }
    }

    public UserResponseSearchUser searchUser(String keyword) {

        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("keyword cannot be empty");
        }

        List<User> users = userRepository.findByNicknameContaining(keyword);

        if (users.isEmpty()) {
            return UserResponseSearchUser.builder()
                    .result("fail")
                    .build();
        }

        List<UserResponseSearchUserResult> results = users.stream()
                .map(user -> UserResponseSearchUserResult.builder()
                        .user_id(user.getId())
                        .nickname(user.getNickname())
                        .profile_img(
                                user.getProfileImg() != null ?
                                        user.getProfileImg().getImgUrl() : null
                        )
                        .build()
                )
                .toList();

        return UserResponseSearchUser.builder()
                .result("success")
                .results(results)
                .build();
    }
    public UserResponseTop5Genre getTop5Genre(Long userId) {
        try {
            if (userId == null) throw new IllegalArgumentException("user_id cannot be null");

            List<UserGenre> userGenres = userGenreRepository.findByUser_Id(userId);

            List<UserResponseTop5Genre.TopGenreDto> top5 = userGenres.stream()
                    .map(ug -> UserResponseTop5Genre.TopGenreDto.builder()
                            .genre_id(ug.getGenre().getId())
                            .genre_name(ug.getGenre().getGenreName())
                            .build())
                    .toList();

            return UserResponseTop5Genre.builder()
                    .result("success")
                    .top5_genres(top5)
                    .build();
        } catch (Exception e) {
            return UserResponseTop5Genre.builder()
                    .result("fail")
                    .top5_genres(Collections.emptyList())
                    .build();
        }
    }


    public UserResponseTop5Genre updateTop5Genre(Long userId, UserRequestUpdateTop5Genre req) {
        try {
            if (userId == null) {
                throw new IllegalArgumentException("user_id cannot be null");
            }
            if (req == null || req.getGenre_name() == null) {
                throw new IllegalArgumentException("genre_names cannot be null");
            }
            if (req.getGenre_name().size() != 5) {
                throw new IllegalArgumentException("genre_names must have exactly 5 items");
            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 1) 장르명 trim + 유효성 + 중복 체크
            List<String> names = req.getGenre_name().stream()
                    .map(s -> s == null ? "" : s.trim())
                    .toList();

            if (names.stream().anyMatch(String::isBlank)) {
                throw new IllegalArgumentException("genre_names must not contain blank items");
            }

            Set<String> dedup = new HashSet<>(names);
            if (dedup.size() != 5) {
                throw new IllegalArgumentException("genre_names must not contain duplicates");
            }

            List<Genre> genres = names.stream()
                    .map(name -> {
                        Genre g = genreRepository.findByGenreName(name);
                        if (g == null) throw new RuntimeException("Genre not found: " + name);
                        return g;
                    })
                    .toList();

            // 2) 기존 top5 삭제 후 새로 저장
            userGenreRepository.deleteByUser_Id(user.getId());

            List<UserGenre> userGenres = genres.stream()
                    .map(g -> UserGenre.builder()
                            .user(user)
                            .genre(g)
                            .build())
                    .toList();

            userGenreRepository.saveAll(userGenres);

            // 3) 응답 구성
            List<UserResponseTop5Genre.TopGenreDto> top5 = genres.stream()
                    .map(g -> UserResponseTop5Genre.TopGenreDto.builder()
                            .genre_id(g.getId())
                            .genre_name(g.getGenreName())
                            .build())
                    .toList();

            return UserResponseTop5Genre.builder()
                    .result("success")
                    .top5_genres(top5)
                    .build();

        } catch (Exception e) {
            e.printStackTrace(); // 디버깅용(필수)
            return UserResponseTop5Genre.builder()
                    .result("fail")
                    .top5_genres(Collections.emptyList())
                    .build();
        }
    }


}
