package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedResponseReviewUpdate {
    private String result;
    private String comment;
    private Float rating;
}
