package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseCheckNickname {
    private boolean available;
}
