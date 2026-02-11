package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;
import org.example.taesejeanhwan_backend.domain.Content;

@Data
@Builder
public class FeedResponseGetWish {
    private String title;
    private String poster;
    private Long content_id;
    private String overview;
    private int year;

    public static FeedResponseGetWish from(Content content) {
        return FeedResponseGetWish.builder()
                .title(content.getTitle())
                .poster(content.getPoster())
                .content_id(content.getId())
                .overview(content.getOverview())
                .year(content.getYear())
                .build();
    }
}
