package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.*;
import org.example.taesejeanhwan_backend.dto.feed.request.FeedRequestAddReview;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetContent;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseGetReview;
import org.example.taesejeanhwan_backend.dto.feed.response.FeedResponseResult;
import org.example.taesejeanhwan_backend.dto.user.GenreStatDto;
import org.example.taesejeanhwan_backend.dto.user.request.UserRequestSetPreferenceContent;
import org.example.taesejeanhwan_backend.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final UserRepository userRepository;
    private final ContentRepository contentRepository;
    private final UserContentRepository userContentRepository;
    private final ContentGenreRepository contentGenreRepository;

    private final ReviewRepository reviewRepository;
    private final GenreRepository genreRepository;

    public FeedResponseResult addReview(FeedRequestAddReview feedRequestAddReview) {
        Content reviewContent = contentRepository.findById(feedRequestAddReview.getContent_id())
                .orElseThrow(() -> new RuntimeException("Content not found"));
        User reviewUser = userRepository.findById(feedRequestAddReview.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Review.builder()
                .comment(feedRequestAddReview.getComment())
                .rating(feedRequestAddReview.getRating())
                .user(reviewUser)
                .content(reviewContent)
                .build();

        //UserContents 영화 다 불러오기
        List<Content> contents = userContentRepository.findAllContent();

        Map<Long, Float> ratingMap = contents
                .stream()
                .collect(Collectors.toMap(

                        UserRequestSetPreferenceContent::getRating
                ));

        // 순위 게산하기

        //UserGenre와 비교하기,
          // 비교할 때 UserContents 순위 계산한 거 1-5 중에 없는게
          // UserGenre에 있을 시 그 장르 아이디와 rating(장르별평점) 을 저장해놓기
        //리뷰 추가한 콘텐츠도 더해서 다시 순위 계산하기
        //순위 계산한거 1-5 중에서 따로 저장해놓은 키워드(사용자가 수정한) 를 제외하고 남은 자리만큼 순위 메기기
        //순위 + 저장해놓은거 가 새로운 userGenre


    }

    public FeedResponseGetContent getContentReview(Long user_id, Long content_id) {
        User user = userRepository.findByUser_id(user_id);
        Content content = contentRepository.findByContentId(content_id);
        Review review = reviewRepository.findByContentAndUser(content, user);
        List<Long> genreIds = contentGenreRepository.findGenre_IdByContent(content);
        List<String> genreNames = new ArrayList<>();
        for(Long id:genreIds){
            genreNames.add(genreRepository.findGenreById(id).getGenre_name());
        }

        return FeedResponseGetContent.builder()
                .title(content.getTitle())
                .poster(content.getPoster())
                .overview(content.getOverview())
                .rating(review.getRating())
                .year(content.getYear())
                .comment(review.getComment())
                .genre_name(genreNames)
                .create_time(review.getCreate_time())
                .build();
    }

    public List<FeedResponseGetReview> getAllReviews(Long content_id) {
        Content content = contentRepository.findByContentId(content_id);
        List<Review> reviews = reviewRepository.findByContent(content);
        return reviews.stream()
                .map(FeedResponseGetReview::from)
                .toList();
    }
}
