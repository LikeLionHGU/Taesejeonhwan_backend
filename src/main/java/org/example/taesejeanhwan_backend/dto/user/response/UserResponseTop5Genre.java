package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserResponseTop5Genre {
    private String result;
    private List<TopGenreDto> top5_genres;

    @Getter
    @Builder
    public static class TopGenreDto {
        private Long genre_id;
        private String genre_name;
    }
}
