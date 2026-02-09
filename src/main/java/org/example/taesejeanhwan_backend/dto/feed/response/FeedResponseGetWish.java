package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedResponseGetWish {
    private String title;
    private String poster;
    private Long content_id;
}
