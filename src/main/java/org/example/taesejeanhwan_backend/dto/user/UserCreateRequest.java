package org.example.taesejeanhwan_backend.dto.user;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

public class UserCreateRequest {

    @NotBlank(message = "이름은 필수입니다.") // null X, 빈 문자열 X, 공백 X
    private String nickname;

    @Email(message = "이메일 형식이 아닙니다.") // 이메일 형식 검증.
    @NotBlank(message = "이메일은 필수입니다.")
    private String email;
}
