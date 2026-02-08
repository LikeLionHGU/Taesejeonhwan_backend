package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedResponseGetUserContent {
    private String title;
    private String poster;
    private Float rating;
}
