package org.example.taesejeanhwan_backend.dto.feed.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedRequestAddWish {
    private Long user_id;
    private Long content_id;
}

