package org.example.taesejeanhwan_backend.dto.feed.response;

import lombok.Builder;
import lombok.Data;
import org.example.taesejeanhwan_backend.domain.Genre;

@Data
@Builder
public class FeedResponseGetGenre {
    private String genre_name;

    public static FeedResponseGetGenre from(Genre genre) {
        return FeedResponseGetGenre.builder()
                .genre_name(genre.getGenre_name()) // Genre 엔티티 필드명에 맞추기
                .build();
    }
}

