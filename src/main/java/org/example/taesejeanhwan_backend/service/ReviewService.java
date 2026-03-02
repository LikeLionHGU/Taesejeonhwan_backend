package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.*;
import org.example.taesejeanhwan_backend.dto.feed.request.FeedRequestAddReview;
import org.example.taesejeanhwan_backend.dto.feed.request.FeedRequestReviewUpdate;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetContent;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetReview;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseResult;
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
    private final UserWishRepository userWishRepository;
    private final UserContentRepository userContentRepository;

    public FeedResponseReviewUpdate updateReview(FeedRequestReviewUpdate req) {

        // 1) 유저/콘텐츠 찾기
        User user = userRepository.findById(req.getUser_id())
                .orElseThrow(() -> new RuntimeException("User Not Found"));
        if (user == null) throw new RuntimeException("User not found");

        Content content = contentRepository.findById(req.getContent_id())
                .orElseThrow(() -> new RuntimeException("Content Not Found"));
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

            List<Review> beforeReviews = reviewRepository.findByUser(reviewUser);
            List<Genre> beforeTop5 = calculateTopGenres(beforeReviews);
            if (beforeTop5 == null) beforeTop5 = new ArrayList<>();

            List<UserGenre> existingUserGenres = userGenreRepository.findByUser(reviewUser);

            Set<Genre> backupGenres = new HashSet<>();
            for (UserGenre ug : existingUserGenres) {
                if (!beforeTop5.contains(ug.getGenre())) {
                    backupGenres.add(ug.getGenre());
                }
            }

            Review review = reviewRepository.findByContentAndUser(reviewContent, reviewUser);

            if (review == null) {
                review = Review.builder()
                        .user(reviewUser)
                        .content(reviewContent)
                        .createTime(LocalDate.now())
                        .build();
            }

            // 최신 값으로 덮어쓰기 (리뷰 수정 포함)
            review.setComment(feedRequestAddReview.getComment());
            review.setRating(feedRequestAddReview.getRating());

            reviewRepository.save(review);

            // ✅ 추가 기능: 내 영화관에 없으면 추가
            UserContent uc = userContentRepository.findByUserAndContent(reviewUser, reviewContent)
                    .orElse(null);

            if (uc == null) {
                UserContent newUserContent = UserContent.builder()
                        .user(reviewUser)
                        .content(reviewContent)
                        .build();
                userContentRepository.save(newUserContent);
            }

            // 리뷰 포함 후 다시 계산
            List<Review> afterReviews = reviewRepository.findByUser(reviewUser);
            List<Genre> afterTop = calculateTopGenres(afterReviews);
            if (afterTop == null) afterTop = new ArrayList<>();

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
            e.printStackTrace();
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
        genreList.sort(
                Comparator
                        .comparing((Genre g) -> countMap.get(g)).reversed()
                        .thenComparing(g -> ratingSumMap.get(g) / countMap.get(g), Comparator.reverseOrder())
                        .thenComparing(Genre::getId)
        );

        return genreList.stream().limit(5).toList();
    }

    /**
     * ✅ 방법 A-2 적용:
     * 리뷰가 없으면 예외를 던지지 않고, content 정보는 그대로 내려주되
     * rating/comment/create_time 같은 리뷰 관련 필드는 null(또는 기본값)로 내려준다.
     */
    public FeedResponseGetContent getContentReview(Long user_id, Long content_id) {

        if (user_id == null || content_id == null) {
            throw new IllegalArgumentException("Invalid request parameter");
        }

        User user = userRepository.findById(user_id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Content content = contentRepository.findById(content_id)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        // 리뷰가 없을 수도 있으니 null 허용
        Review review = reviewRepository.findByContentAndUser(content, user);

        // 콘텐츠 장르는 리뷰 유무와 관계없이 반환 가능
        List<String> genreNames = contentGenreRepository.findByContent(content)
                .stream()
                .map(ContentGenre::getGenre)
                .map(Genre::getGenreName)
                .toList();

        // 리뷰가 없으면 리뷰 관련 필드는 비워서 내려준다
        // ⚠️ FeedResponseGetContent의 rating 타입이 primitive(float/int)이면 null 대신 0f/0을 넣어야 함
        Float rating = null;
        String comment = null;
        String formattedDate = null;

        if (review != null) {
            // review.getRating() 타입이 Float이면 그대로, primitive면 캐스팅 맞춰줘
            rating = review.getRating();
            comment = review.getComment();

            if (review.getCreateTime() != null) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                formattedDate = review.getCreateTime().format(formatter);
            }
        }

        return FeedResponseGetContent.builder()
                .title(content.getTitle())
                .poster(content.getPoster())
                .overview(content.getOverview())
                .rating(rating)          // 리뷰 없으면 null (primitive면 0으로 바꿔)
                .year(content.getYear())
                .comment(comment)        // 리뷰 없으면 null
                .wished(userWishRepository.existsByUserAndContent(user, content))
                .genre_name(genreNames)
                .create_time(formattedDate) // 리뷰 없으면 null
                .build();
    }

    public List<FeedResponseGetReview> getAllReviews(Long content_id) {
        if (content_id == null) {
            throw new IllegalArgumentException("Invalid request parameter");
        }
        Content content = contentRepository.findById(content_id)
                .orElseThrow(() -> new RuntimeException("Content not found"));

        List<Review> reviews = reviewRepository.findByContent(content);
        return reviews.stream()
                .map(FeedResponseGetReview::from)
                .toList();
    }
}
