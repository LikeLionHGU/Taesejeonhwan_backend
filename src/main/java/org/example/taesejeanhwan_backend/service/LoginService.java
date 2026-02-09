package org.example.taesejeanhwan_backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.dto.login.LoginResult;
import org.example.taesejeanhwan_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    @Value("${google.client-id}")
    private String googleClientId;

    public LoginResult loginWithGoogleIdToken(String idTokenString) {
        GoogleIdToken.Payload payload = verify(idTokenString);

        String email = payload.getEmail();
        String sub = payload.getSubject(); // google 고유 ID

        // googleSub 기준으로 유저 조회
        User user = userRepository.findByGoogleSub(sub).orElse(null);

        // 유저가 존재하는 경우
        if (user != null) {
            // 닉네임이 없으면 -> 신규 유저로 판단
            boolean is_new_user = (user.getNickname() == null);
            return new LoginResult(user, is_new_user);
        }

        // 완전 신규 회원
        User newUser = User.builder()
                .email(email)
                .googleSub(sub)
                .nickname(null)
                .build();

        User savedUser = userRepository.save(newUser);
        return new LoginResult(savedUser, true);
    }

    // id_token 검증
    private GoogleIdToken.Payload verify(String idTokenString) {
        try {
            GoogleIdTokenVerifier verifier =
                    new GoogleIdTokenVerifier.Builder(
                            GoogleNetHttpTransport.newTrustedTransport(),
                            GsonFactory.getDefaultInstance())
                            .setAudience(Collections.singletonList(googleClientId))
                            .build();

            GoogleIdToken token = verifier.verify(idTokenString);
            if (token == null) {
                throw new IllegalArgumentException("Invalid Google ID Token");
            }
            return token.getPayload();
        } catch (Exception e) {
            throw new IllegalArgumentException("Google ID Token verification failed", e);
        }
    }
}
