package org.example.taesejeanhwan_backend.dto.feed.request;

import lombok.Data;

@Data
public class FeedRequestReviewUpdate {
    private Long user_id;
    private Long content_id;
    private String comment;
    private Float rating;
}
