package org.example.taesejeanhwan_backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
@AllArgsConstructor
public class GenreStatDto {
    private Long genreId;
    private long count;
    private double ratingSum;
    private long minContentId;

    public double getAvgRating() {
        return ratingSum / count;
    }

}
