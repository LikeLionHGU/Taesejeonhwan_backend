package org.example.taesejeanhwan_backend.dto.login;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank
    private String idToken;
}