package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedResponseSearchUserResult {
    private Long user_id;
    private String nickname;
    private String profile_img;
}
