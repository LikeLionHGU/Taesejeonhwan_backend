package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.*;
import org.example.taesejeanhwan_backend.dto.feed.response.*;
import org.example.taesejeanhwan_backend.dto.user.response.UserResponseContent;
import org.example.taesejeanhwan_backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ContentService {

    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final UserContentRepository userContentRepository;
    private final UserGenreRepository userGenreRepository;
    private final ReviewRepository reviewRepository;
    private final GenreRepository genreRepository;
    private final FollowRepository followRepository;

    public List<UserResponseContent> getContentsForSelection() {
        List<Content> contents = contentRepository.findAll();
        return contents.stream()
                .map(UserResponseContent::from)
                .limit(400)
                .toList();
    }

    /**
     * @param mode "similar" or "diff"
     * @param loginUserId 로그인한 사용자 id (me)
     * @param targetUserId 컨트롤러에서 넘어오지만, 현재 로직은 "나 기준 추천"이라 필수로 쓰진 않음
     * @param page 페이지 (0부터)
     */
    public FeedResponseUserAndContent getUserAndContent(String mode,
                                                        Long loginUserId,
                                                        int page) {

        try {
            int pageSize = 50;

            if (mode == null || (!mode.equals("similar") && !mode.equals("diff"))) {
                throw new IllegalArgumentException("Invalid mode");
            }

            if (loginUserId == null) {
                throw new IllegalArgumentException("loginUserId cannot be null");
            }

            if (page < 0) {
                throw new IllegalArgumentException("Page cannot be negative");
            }

            // ✅ 나(me)
            User me = userRepository.findById(loginUserId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // ✅ 내 장르 id 집합
            Set<Long> myGenreIds = userGenreRepository.findByUser_Id(loginUserId).stream()
                    .map(ug -> ug.getGenre().getId())
                    .collect(Collectors.toSet());

            // 전체 유저 가져오기 (나 제외)
            List<User> allUsers = userRepository.findAll().stream()
                    .filter(u -> !u.getId().equals(loginUserId))
                    .toList();

            // 조건에 맞는 유저 필터링
            List<User> filteredUsers = new ArrayList<>();

            for (User user : allUsers) {
                Set<Long> otherGenreIds = userGenreRepository.findByUser_Id(user.getId()).stream()
                        .map(ug -> ug.getGenre().getId())
                        .collect(Collectors.toSet());

                Set<Long> intersection = new HashSet<>(myGenreIds);
                intersection.retainAll(otherGenreIds);

                int overlapCount = intersection.size();

                boolean match = mode.equals("similar") ? overlapCount >= 3 : overlapCount <= 1;

                if (match) filteredUsers.add(user);
            }

            // 페이지 계산
            int start = page * pageSize;
            int end = Math.min(start + pageSize, filteredUsers.size());

            if (start >= filteredUsers.size()) {
                return FeedResponseUserAndContent.builder()
                        .mode(mode)
                        .page(page)
                        .hasNext(false)
                        .feeds(List.of())
                        .build();
            }

            List<User> pageUsers = filteredUsers.subList(start, end);

            // DTO 변환
            List<FeedResponseUserAndContentGetUser> feeds = new ArrayList<>();

            for (User user : pageUsers) {

                List<String> genreNames = userGenreRepository.findByUser_Id(user.getId())
                        .stream()
                        .map(g -> g.getGenre().getGenreName().trim().toLowerCase())
                        .toList();

                List<UserContent> userContents = userContentRepository.findAllByUser(user);

                // ✅ 이 메서드 아래에 추가되어 있음
                List<Content> contents = selectFiveContents(userContents, user);

                List<FeedResponseUserAndContentGetContent> contentDtos =
                        contents.stream()
                                .limit(5)
                                .map(c -> FeedResponseUserAndContentGetContent.builder()
                                        .content_id(c.getId())
                                        .title(c.getTitle())
                                        .poster(c.getPoster())
                                        .build())
                                .toList();

                // ✅ 내가 이 유저를 팔로우 중인지
                boolean isFollowing = followRepository.existsByUserAndAnotherUser(me, user);

                feeds.add(
                        FeedResponseUserAndContentGetUser.builder()
                                .user_id(user.getId())
                                .nickname(user.getNickname())
                                .profile_img(
                                        user.getProfileImg() != null
                                                ? user.getProfileImg().getImgUrl()
                                                : null
                                )
                                .is_following(isFollowing) // ✅ DTO에 필드 필요!
                                .genre_keyword(genreNames)
                                .content(contentDtos)
                                .build()
                );
            }

            boolean hasNext = end < filteredUsers.size();

            return FeedResponseUserAndContent.builder()
                    .mode(mode)
                    .page(page)
                    .hasNext(hasNext)
                    .feeds(feeds)
                    .build();

        } catch (Exception e) {
            return FeedResponseUserAndContent.builder()
                    .mode(mode)
                    .page(page)
                    .hasNext(false)
                    .feeds(List.of())
                    .build();
        }
    }

    // ✅ 반드시 ContentService 클래스 안에 있어야 함
    public List<Content> selectFiveContents(List<UserContent> userContents, User user) {

        List<Content> contents = userContents.stream()
                .map(UserContent::getContent)
                .toList();

        if (contents.isEmpty()) return List.of();

        // ✅ ReviewRepository에 이 메서드가 있어야 함:
        // List<Review> findTop5ByUserAndContentInOrderByRatingDescCreateTimeDesc(User user, List<Content> contents);
        List<Review> topReviews =
                reviewRepository.findTop5ByUserAndContentInOrderByRatingDescCreateTimeDesc(user, contents);

        return topReviews.stream()
                .map(Review::getContent)
                .distinct()
                .limit(5)
                .toList();
    }

    public FeedResponseSearchContent searchContent(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Invalid keyword");
        }
        List<Content> contents = contentRepository.findAllByTitleContaining(keyword);
        if (contents.isEmpty()) {
            return FeedResponseSearchContent.builder()
                    .result("fail")
                    .build();
        }
        return FeedResponseSearchContent.builder()
                .result("success")
                .results(contents.stream()
                        .map(FeedResponseSearchContentResult::from)
                        .toList())
                .build();
    }

    public List<FeedResponseGetGenre> getAllGenres() {
        List<Genre> genres = genreRepository.findAll();
        return genres.stream()
                .map(FeedResponseGetGenre::from)
                .toList();
    }
}