package org.example.taesejeanhwan_backend.dto.feed.request;

import lombok.Data;

@Data
public class FeedRequestReviewDelete {
    private Long user_id;
    private Long content_id;
}
