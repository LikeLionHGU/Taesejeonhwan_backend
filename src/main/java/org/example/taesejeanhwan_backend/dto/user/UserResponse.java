package org.example.taesejeanhwan_backend.dto.user;

import org.example.taesejeanhwan_backend.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor // 모든 필드를 받는 생성자 자동 생성.
public class UserResponse {
    private Long id;
    private String nickname;

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getNickname());
    }
}