package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FeedResponseGetContent {
    private String title;
    private String poster;
    private String overview;
    private Float rating;
    private int year;
    private boolean is_wished;
    private String comment;
    private List<String> genre_name;
    private String create_time;
}
