package org.example.taesejeanhwan_backend.dto.user.request;

import lombok.Data;

@Data
public class UserRequestSetNickname {
    private Long user_id;
    private String nickname;
}
