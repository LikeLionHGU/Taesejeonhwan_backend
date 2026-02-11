package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;
import org.example.taesejeanhwan_backend.domain.Review;

@Data
@Builder
public class FeedResponseGetReview {
    private Long user_id;
    private String comment;
    private Float rating;

    public static FeedResponseGetReview from(Review review) {
        return FeedResponseGetReview.builder()
                .user_id(review.getUser().getId())
                .comment(review.getComment())
                .rating(review.getRating())
                .build();
    }
}
