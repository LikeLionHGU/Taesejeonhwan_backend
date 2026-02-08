package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedResponseUserAndContentGetUser {
    private Long user_id;
    private String nickname;
    private String profile_img;
    private List<FeedResponseUserAndContentGetContent> content;
}
