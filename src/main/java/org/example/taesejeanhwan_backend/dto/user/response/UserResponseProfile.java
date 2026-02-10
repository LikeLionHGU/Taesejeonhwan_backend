package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseProfile {
    private String result;
    private String nickname;
    private String profile_img;
    private UserResponseProfileDataFollow stats;
    private List<UserResponseProfileGenre> table;
}
