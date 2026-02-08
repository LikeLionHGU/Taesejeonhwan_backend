package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedResponseAddWish {
    private Long user_id;
    private Long content_id;
}
