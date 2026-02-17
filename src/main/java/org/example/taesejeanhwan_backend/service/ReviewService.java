package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.*;
import org.example.taesejeanhwan_backend.dto.feed.request.FeedRequestAddReview;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetContent;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetReview;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseResult;
import org.example.taesejeanhwan_backend.dto.feed.request.FeedRequestReviewUpdate;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseReviewUpdate;
import org.example.taesejeanhwan_backend.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final ContentGenreRepository contentGenreRepository;
    private final UserGenreRepository userGenreRepository;
    private final ReviewRepository reviewRepository;
    private final GenreRepository genreRepository;
    private final UserWishRepository userWishRepository;
    public FeedResponseReviewUpdate updateReview(FeedRequestReviewUpdate req) {

        // 1) 유저/콘텐츠 찾기 (너 서비스에서 이미 쓰던 방식 그대로)
        User user = userRepository.findById(req.getUser_id()).orElseThrow(()->new RuntimeException("User Not Found"));
        if (user == null) throw new RuntimeException("User not found");

        Content content = contentRepository.findByContentId(req.getContent_id());
        if (content == null) throw new RuntimeException("Content not found");

        // 2) 해당 유저가 해당 콘텐츠에 남긴 리뷰 찾기
        Review review = reviewRepository.findByContentAndUser(content, user);
        if (review == null) throw new RuntimeException("Review not found");

        // 3) 수정 (요청값이 있는 것만 변경)
        if (req.getComment() != null) {
            review.setComment(req.getComment());
        }
        if (req.getRating() != null) {
            review.setRating(req.getRating());
        }

        // 4) 저장
        Review saved = reviewRepository.save(review);

        // 5) 응답
        return FeedResponseReviewUpdate.builder()
                .result("success")
                .comment(saved.getComment())
                .rating(saved.getRating())
                .build();
    }

    public FeedResponseResult addReview(FeedRequestAddReview feedRequestAddReview) {
        try {
            if (feedRequestAddReview == null) {
                throw new IllegalArgumentException("Request is null");
            }

            if (feedRequestAddReview.getRating() < 0 || feedRequestAddReview.getRating() > 5) {
                throw new IllegalArgumentException("Rating must be between 0 and 5");
            }

            Content reviewContent = contentRepository.findById(feedRequestAddReview.getContent_id())
                    .orElseThrow(() -> new RuntimeException("Content not found"));

            User reviewUser = userRepository.findById(feedRequestAddReview.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            // 리뷰 추가 전 장르 순위 계산
            List<Review> beforeReviews = reviewRepository.findByUser(reviewUser);
            List<Genre> beforeTop5 = calculateTopGenres(beforeReviews);

            if (beforeTop5 == null) {
                beforeTop5 = new ArrayList<>();
            }

            // 기존 UserGenre와 비교해서 안 겹치는 것 백업
            List<UserGenre> existingUserGenres = userGenreRepository.findByUser(reviewUser);

            Set<Genre> backupGenres = new HashSet<>();
            for (UserGenre ug : existingUserGenres) {
                if (!beforeTop5.contains(ug.getGenre())) {
                    backupGenres.add(ug.getGenre());
                }
            }

            // 신규 리뷰 저장
            Review review = Review.builder()
                    .comment(feedRequestAddReview.getComment())
                    .rating(feedRequestAddReview.getRating())
                    .user(reviewUser)
                    .content(reviewContent)
                    .createTime(LocalDate.now())
                    .build();
            reviewRepository.save(review);

            // 리뷰 포함 후 다시 계산
            List<Review> afterReviews = reviewRepository.findByUser(reviewUser);
            List<Genre> afterTop = calculateTopGenres(afterReviews);

            if (afterTop == null) {
                afterTop = new ArrayList<>();
            }

            // 백업 장르 포함해서 최종 5개 구성
            List<Genre> finalTop5 = new ArrayList<>();

            int limit = Math.max(0, 5 - backupGenres.size());

            for (Genre g : afterTop) {
                if (finalTop5.size() >= limit) break;
                finalTop5.add(g);
            }

            for (Genre backup : backupGenres) {
                if (finalTop5.size() >= 5) break;
                if (!finalTop5.contains(backup)) {
                    finalTop5.add(backup);
                }
            }

            // UserGenre 갱신
            userGenreRepository.deleteByUser(reviewUser);

            for (Genre genre : finalTop5) {
                UserGenre userGenre = UserGenre.builder()
                        .user(reviewUser)
                        .genre(genre)
                        .build();
                userGenreRepository.save(userGenre);
            }

            return FeedResponseResult.builder()
                    .result("success")
                    .build();

        } catch (Exception e) {

            return FeedResponseResult.builder()
                    .result("error")
                    .build();
        }
    }

    private List<Genre> calculateTopGenres(List<Review> reviews) {
        Map<Genre, Integer> countMap = new HashMap<>();
        Map<Genre, Float> ratingSumMap = new HashMap<>();

        for (Review r : reviews) {
            List<ContentGenre> contentGenres = contentGenreRepository.findByContent(r.getContent());
            for (ContentGenre cg : contentGenres) {
                Genre genre = cg.getGenre();
                countMap.put(genre, countMap.getOrDefault(genre, 0) + 1);
                ratingSumMap.put(genre, ratingSumMap.getOrDefault(genre, 0f) + r.getRating());
            }
        }

        List<Genre> genreList = new ArrayList<>(countMap.keySet());
        //많이 겹치는 순, 평균 평점 높은 순, 장르 아이디 작은 순으로 정렬
        genreList.sort(
                Comparator
                        .comparing((Genre g) -> countMap.get(g)).reversed()
                        .thenComparing(g -> ratingSumMap.get(g) / countMap.get(g), Comparator.reverseOrder())
                        .thenComparing(Genre::getId)
        );

        return genreList.stream().limit(5).toList();
    }

    public FeedResponseGetContent getContentReview(Long user_id, Long content_id) {
        try {
            if (user_id == null || content_id == null) {
                throw new IllegalArgumentException("Invalid request parameter");
            }
            User user = userRepository.findById(user_id).orElseThrow(() -> new RuntimeException("User not found"));
            Content content = contentRepository.findById(content_id).orElseThrow(() -> new RuntimeException("Content not found"));
            Review review = reviewRepository.findByContentAndUser(content, user);

            if (review == null) {
                throw new RuntimeException("Review not found");
            }

            List<Long> genreIds = contentGenreRepository.findGenre_IdByContent(content);
            List<String> genreNames = new ArrayList<>();
            for (Long id : genreIds) {
                Genre genre = genreRepository.findGenreById(id);

                if (genre != null) {
                    genreNames.add(genre.getGenre_name());
                }
            }

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = review.getCreateTime().format(formatter);

            return FeedResponseGetContent.builder()
                    .title(content.getTitle())
                    .poster(content.getPoster())
                    .overview(content.getOverview())
                    .rating(review.getRating())
                    .year(content.getYear())
                    .comment(review.getComment())
                    .is_wished(userWishRepository.existsByUserAndContent(user, content))
                    .genre_name(genreNames)
                    .create_time(formattedDate)
                    .build();

        } catch (Exception e) {
            return FeedResponseGetContent.builder()
                    .title(null)
                    .poster(null)
                    .overview(null)
                    .rating(0f)
                    .year(0)
                    .comment("Error occurred")
                    .is_wished(false)
                    .genre_name(List.of())
                    .create_time(null)
                    .build();
        }
    }

    public List<FeedResponseGetReview> getAllReviews(Long content_id) {
        if (content_id == null) {
            throw new IllegalArgumentException("Invalid request parameter");
        }
        Content content = contentRepository.findById(content_id).orElseThrow(()->new RuntimeException("Content not found"));
        List<Review> reviews = reviewRepository.findByContent(content);
        return reviews.stream()
                .map(FeedResponseGetReview::from)
                .toList();
    }
}
