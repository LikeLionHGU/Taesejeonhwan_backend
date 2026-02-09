package org.example.taesejeanhwan_backend.dto.user;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserCreateRequest {

    @NotBlank(message = "이름은 필수입니다.") // null X, 빈 문자열 X, 공백 X
    private String nickname;
}
