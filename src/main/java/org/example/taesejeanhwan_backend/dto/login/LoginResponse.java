package org.example.taesejeanhwan_backend.dto.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {
    private String result;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("is_new_user")
    private boolean newUser;

    private String nickname;
}