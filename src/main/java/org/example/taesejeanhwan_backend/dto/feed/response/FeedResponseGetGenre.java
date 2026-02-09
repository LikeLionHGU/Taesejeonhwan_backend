package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FeedResponseGetGenre {
    private String genre_name;
}
