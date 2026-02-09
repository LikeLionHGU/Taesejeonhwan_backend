package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedResponseUserAndContentGetContent {
    private Long content_id;
    private String title;
    private String poster;
}
