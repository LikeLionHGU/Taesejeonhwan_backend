package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedResponseUserAndContent {
    private String mode;
    private Long page;
    private List<FeedResponseUserAndContentGetUser> feeds;
}
