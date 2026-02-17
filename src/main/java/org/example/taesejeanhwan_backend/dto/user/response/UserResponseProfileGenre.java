package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Data;
import org.example.taesejeanhwan_backend.domain.UserGenre;

@Data
@Builder
public class UserResponseProfileGenre {
    private String keyword;

    public static UserResponseProfileGenre from(UserGenre userGenre) {
        return UserResponseProfileGenre.builder()
                .keyword(userGenre.getGenre().getGenreName())
                .build();
    }
}
