package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;
import org.example.taesejeanhwan_backend.domain.Content;

@Data
@Builder
public class FeedResponseSearchContentResult {
    private Long content_id;
    private String title;
    private String poster;
    private int year;

    public static FeedResponseSearchContentResult from(Content content) {
        return FeedResponseSearchContentResult.builder()
                .content_id(content.getId())
                .title(content.getTitle())
                .poster(content.getPoster())
                .year(content.getYear())
                .build();
    }
}
