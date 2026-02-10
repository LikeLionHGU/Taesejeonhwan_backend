package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseProfileDataFollow {
    private int follower_count;
    private int following_count;
}
