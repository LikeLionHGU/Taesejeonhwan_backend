package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Data;
import org.example.taesejeanhwan_backend.domain.Content;

@Data
@Builder
public class UserResponseContent {
    private Long content_id;
    private String title;
    private String poster;

    public static UserResponseContent from(Content content) {
        return builder()
                .content_id(content.getId())
                .title(content.getTitle())
                .poster(content.getPoster())
                .build();
    }
}
