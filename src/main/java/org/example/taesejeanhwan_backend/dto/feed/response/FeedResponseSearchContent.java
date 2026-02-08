package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedResponseSearchContent {
    private String title;
    private String poster;
    private int year;
    private List<Long> genre_id;
}
