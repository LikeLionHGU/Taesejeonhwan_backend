package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.*;
import org.example.taesejeanhwan_backend.dto.feed.response.*;
import org.example.taesejeanhwan_backend.dto.user.response.UserResponseContent;
import org.example.taesejeanhwan_backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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

    public List<UserResponseContent> getContentsForSelection() {
        List<Content> contents = contentRepository.findAll();
        return contents.stream()
                .map(UserResponseContent::from)
                .limit(100)
                .toList();
    }

    public FeedResponseUserAndContent getUserAndContent(String mode, Long userId, int page) {

        try {
            int pageSize = 20;

            if (mode == null || (!mode.equals("similar") && !mode.equals("diff"))) {
                throw new IllegalArgumentException("Invalid mode");
            }

            if (page < 0) {
                throw new IllegalArgumentException("Page cannot be negative");
            }

            User me = userRepository.findById(userId)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            // 내 장르 가져오기
            List<UserGenre> myGenres = userGenreRepository.findByUser_Id(userId);
            Set<String> myGenreNames = myGenres.stream()
                    .map(g -> g.getGenre().getGenre_name())
                    .collect(Collectors.toSet());

            // 전체 유저 가져오기 (나 제외)
            List<User> allUsers = userRepository.findAll().stream()
                    .filter(u -> !u.getId().equals(userId))
                    .toList();

            // 조건에 맞는 유저 필터링
            List<User> filteredUsers = new ArrayList<>();
            for (User user : allUsers) {
                List<UserGenre> otherGenres =
                        userGenreRepository.findByUser_Id(user.getId());
                Set<String> otherGenreNames = otherGenres.stream()
                        .map(g -> g.getGenre().getGenre_name())
                        .collect(Collectors.toSet());

                Set<String> intersection = new HashSet<>(myGenreNames);
                intersection.retainAll(otherGenreNames);

                int overlapCount = intersection.size();

                boolean match;

                if (mode.equals("similar")) {
                    match = overlapCount >= 4;
                } else { // diff
                    match = overlapCount <= 1;
                }

                if (match) {
                    filteredUsers.add(user);
                }
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
                        .map(g -> g.getGenre().getGenre_name())
                        .toList();

                List<UserContent> userContents = userContentRepository.findAllByUser(user);
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

                feeds.add(
                        FeedResponseUserAndContentGetUser.builder()
                                .user_id(user.getId())
                                .nickname(user.getNickname())
                                .profile_img(
                                        user.getProfileImg() != null ?
                                                user.getProfileImg().getImgUrl() : null
                                )
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

    public List<Content> selectFiveContents(List<UserContent> userContents, User user) {
        List<Content> contents = userContents.stream()
                .map(UserContent::getContent)
                .toList();

        if (contents.isEmpty()) return List.of();

        List<Review> topReviews =
                reviewRepository.findTop5ByUserAndContentInOrderByRatingDescCreateTimeDesc(user, contents);

        return topReviews.stream()
                .map(Review::getContent)
                .distinct()
                .limit(5)
                .toList();
    }

    public FeedResponseSearchContent searchContent(String keyword) {
        if(keyword == null || keyword.isEmpty()) {
            throw new IllegalArgumentException("Invalid keyword");
        }
        List<Content> contents = contentRepository.findAllByTitle(keyword);
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


}