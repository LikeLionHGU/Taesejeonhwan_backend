package org.example.taesejeanhwan_backend.service;


import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.example.taesejeanhwan_backend.domain.User;
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

    public User loginWithGoogleIdToken(String idTokenString) {
        GoogleIdToken.Payload payload = verify(idTokenString);

        String sub = payload.getSubject();
        String nickName = (String) payload.get("nickname");

        //  구글 sub 기준으로 기존 유저 찾기, 예전에 로그인 했던 사람이면 googlesub이 저장되있음.
        User user = userRepository.findByGoogleSub(sub).orElse(null);
        if (user != null) {
            user.updateOAuthProfile(nickName);
            return userRepository.save(user);
        }

        // 완전 신규 회원
        User newUser = User.builder()
                .nickname(nickName != null ? nickName : "google-user")
                .googleSub(sub)
                .build();

        return userRepository.save(newUser);
    }

    // id_token이 진짜인지 검증
    private GoogleIdToken.Payload verify(String idTokenString) {
        try {
            //GoogleIdTokenVerifier: 구글이 제공하는 검증 도구
            GoogleIdTokenVerifier verifier =
                    new GoogleIdTokenVerifier.Builder(
                            GoogleNetHttpTransport.newTrustedTransport(),
                            GsonFactory.getDefaultInstance())
                            .setAudience(Collections.singletonList(googleClientId))
                            .build();

            //검증 성공하면 token 객체 반환
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
