package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseSearchUserResult {
    private Long user_id;
    private String nickname;
    private String profile_img;
}
