package org.example.taesejeanhwan_backend.service;

import org.example.taesejeanhwan_backend.domain.*;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseSearchContent;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseSearchContentResult;
import org.example.taesejeanhwan_backend.dto.user.GenreStatDto;
import org.example.taesejeanhwan_backend.dto.feed.request.FeedRequestUpdateGenre;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseResult;
import org.example.taesejeanhwan_backend.dto.user.request.*;
import org.example.taesejeanhwan_backend.dto.user.response.*;
import org.example.taesejeanhwan_backend.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;;
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

    public UserResponseResult setProfileImage(UserRequestSetImage userRequestSetImage) {
        try {
            User user = userRepository.findById(userRequestSetImage.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            ProfileImg profile = profileImgRepository.findByImgUrl(userRequestSetImage.getProfile_img());
            user.builder()
                    .profileImg(profile)
                    .build();
            userRepository.save(user);

            String resultReturn;
            if(userRepository.existsByIdAndProfileImg(userRequestSetImage.getUserId(), profile)) {
                resultReturn = "success";
            }
            else {
                resultReturn = "fail";
            }
            return UserResponseResult.builder()
                    .result(resultReturn)
                    .build();
        } catch (Exception e) {
            return UserResponseResult.builder()
                    .result("fail")
                    .build();
        }
    }

    public UserResponseResult setNickname(UserRequestSetNickname userRequestSetNickname) {
        if (userRequestSetNickname.getNickname() == null) {
            throw new IllegalArgumentException("nickname cannot be null");
        }
        if (userRequestSetNickname.getUser_id() == null) {
            throw new IllegalArgumentException("user_id cannot be null");
        }
        User user = userRepository.findById(userRequestSetNickname.getUser_id()).orElseThrow(() -> new RuntimeException("User not found"));
        user.builder()
                .nickname(userRequestSetNickname.getNickname())
                .build();
        userRepository.save(user);

        String resultReturn;
        if(userRepository.existsByIdAndNickname(userRequestSetNickname.getUser_id(), userRequestSetNickname.getNickname())) {
            resultReturn = "success";
        }
        else {
            resultReturn = "fail";
        }
        return UserResponseResult.builder()
                .result(resultReturn)
                .build();
    }

    public UserResponseResult setPreference(UserRequestSetPreference userRequestSetPreference) {
        try {
            //content_id와 rating map하기
            Map<Long, Float> ratingMap = userRequestSetPreference.getUser_contents()
                    .stream()
                    .collect(Collectors.toMap(
                            UserRequestSetPreferenceContent::getContent_id,
                            UserRequestSetPreferenceContent::getRating
                    ));
            //content_id모음
            List<Long> contentIds = new ArrayList<>(ratingMap.keySet());
            LocalDate localDate = LocalDate.now();

            User reviewUser = userRepository.findById(userRequestSetPreference.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            List<Content> contents = contentRepository.findAllById(contentIds);
            Map<Long, Content> contentMap = contents.stream()
                    .collect(Collectors.toMap(Content::getId, c -> c));

            for (int i = 0; i < contentIds.size(); i++) {
                Long contentId = contentIds.get(i);
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
            //content_id로 해당하는 ContentGenre 리스트업
            List<ContentGenre> contentGenres = contentGenreRepository.findAllByContent_IdIn(contentIds);
            //genre_id와 장릐의 통계(횟수, 별점, 최소 content_id) 매핑하기
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

            //상위 5개 장르 리스트업
            List<GenreStatDto> top5Genres = genreStatMap.values().stream()
                    .sorted(
                            Comparator
                                    .comparingLong(GenreStatDto::getCount).reversed()
                                    .thenComparingDouble(GenreStatDto::getAvgRating).reversed()
                                    .thenComparingLong(GenreStatDto::getMinContentId)
                    )
                    .limit(5)
                    .toList();
            userGenreRepository.deleteByUser_Id(userRequestSetPreference.getUser_id());

            //UserGenre 만들기 및 저장
            List<UserGenre> userGenres = top5Genres.stream()
                    .map(stat -> UserGenre.builder()
                            .user(userRepository.getReferenceById(userRequestSetPreference.getUser_id()))
                            .genre(genreRepository.getReferenceById(stat.getGenreId()))
                            .build()
                    )
                    .toList();
            userGenreRepository.saveAll(userGenres);

            return UserResponseResult.builder()
                    .result("success")
                    .build();

        } catch (Exception e) {
            return UserResponseResult.builder()
                    .result("fail")
                    .build();
        }
    }

    public UserResponseCheckNickname checkNickname(UserRequestCheckNickname userRequestCheckNickname) {
        if(userRequestCheckNickname.getNickname().isEmpty()) {
            throw new IllegalArgumentException("nickname cannot be empty");
        }
        if(userRepository.existsByNickname(userRequestCheckNickname.getNickname())) {
            return UserResponseCheckNickname.builder()
                    .is_available(false)
                    .build();
        }
        return UserResponseCheckNickname.builder()
                .is_available(true)
                .build();
    }

    public List<UserResponseProfileImage> getAllProfileImages() {
        List<ProfileImg> profileImgs = profileImgRepository.findAll();
        return profileImgs.stream()
                .map(UserResponseProfileImage::from)
                .toList();
    }

    public UserResponseProfile getProfile(Long user_id) {
        if (user_id == null) {
            throw new IllegalArgumentException("user_id cannot be null");
        }
        try {
            User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));

            int followerCount = followRepository.findAllByUser_id(user_id).size();
            int followingCount = followRepository.findAllByAnotherUser_Id(user_id).size();

            List<UserResponseProfileGenre> table = userGenreRepository.findByUser_Id(user_id).stream()
                    .map(UserResponseProfileGenre::from)
                    .toList();

            return UserResponseProfile.builder()
                    .result("success")
                    .nickname(user.getNickname())
                    .profile_img(user.getProfileImg().getImgUrl())
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
        if (!userRepository.existsByNickname(keyword)) {
            return UserResponseSearchUser.builder()
                    .result("fail")
                    .build();
        }
        User user = userRepository.findByNickname(keyword);

        List<UserResponseSearchUserResult> users = new ArrayList<>();
        users.add(
                UserResponseSearchUserResult.builder()
                        .user_id(user.getId())
                        .nickname(user.getNickname())
                        .profile_img(user.getProfileImg().getImgUrl())
                        .build()
        );
        return UserResponseSearchUser.builder()
                .result("success")
                .results(users)
                .build();
    }

    public FeedResponseResult updateKeyword(Long userId, FeedRequestUpdateGenre req) {
        try {
            List<UserGenre> userGenres = userGenreRepository.findByUser_Id(userId);

            UserGenre target = userGenres.stream()
                    .filter(ug -> ug.getGenre().getGenre_name().equals(req.getGenre_name()))
                    .findFirst()
                    .orElse(null);

            if (target == null) {
                return FeedResponseResult.builder().result("fail").build();
            }

            Genre newGenre = genreRepository.findByGenre_name(req.getChanged_genre());
            if (newGenre == null) {
                return FeedResponseResult.builder().result("fail").build();
            }

            target.setGenre(newGenre);

            userGenreRepository.save(target);

            return FeedResponseResult.builder().result("success").build();

        } catch (Exception e) {
            return FeedResponseResult.builder().result("fail").build();
        }
    }


}
