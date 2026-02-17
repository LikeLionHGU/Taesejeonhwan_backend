package org.example.taesejeanhwan_backend.dto.feed.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedRequestAddWish {
    private Long user_id;
    private Long content_id;
}

