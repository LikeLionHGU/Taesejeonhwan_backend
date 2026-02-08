package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedResponseSearch {
    private String result;
    private List<FeedResponseUserAndContentGetContent> results;
}
