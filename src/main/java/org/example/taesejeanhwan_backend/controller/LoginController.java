package org.example.taesejeanhwan_backend.controller;


import org.example.taesejeanhwan_backend.domain.User;

import org.example.taesejeanhwan_backend.dto.login.LoginRequest;
import org.example.taesejeanhwan_backend.dto.login.LoginResponse;
import org.example.taesejeanhwan_backend.service.LoginService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;
    public static final String SESSION_USER_ID = "LOGIN_USER_ID";

    @PostMapping("/google")
    public ResponseEntity<LoginResponse> googleLogin(
            @Valid @RequestBody LoginRequest request,
            HttpSession session
    ) {
        User user = loginService.loginWithGoogleIdToken(request.getIdToken());
        //구글이 준 idToken이 진짜인지 확인.

        //  세션에 로그인 정보 저장
        session.setAttribute(SESSION_USER_ID, user.getId());

        return ResponseEntity.ok(
                new LoginResponse(user.getId(), user.getEmail(), user.getName())
        );
    }

    //현재 로그인 상태
    @GetMapping("/me")
    public ResponseEntity<?> me(HttpSession session) {
        Object userId = session.getAttribute(SESSION_USER_ID);
        if (userId == null) {
            return ResponseEntity.status(401).body("NOT_LOGGED_IN");
        }
        return ResponseEntity.ok(userId);
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpSession session) {
        session.invalidate();//세션 자체를 폐기해버림
        return ResponseEntity.ok().build();
    }
}
