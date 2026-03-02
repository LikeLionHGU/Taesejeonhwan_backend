package org.example.taesejeanhwan_backend.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.dto.login.LoginResult;
import org.example.taesejeanhwan_backend.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;

    @Value("${google.client-id}")
    private String googleClientId;

    // 너네 프로젝트에 있는 것들만 주입해서 사용하면 됨
    private final UserWishRepository userWishRepository;
    private final ReviewRepository reviewRepository;
    private final UserGenreRepository userGenreRepository;
    private final UserContentRepository userContentRepository; // 있으면
    // private final ContentGenreRepository contentGenreRepository; // 유저랑 무관하면 X
    // private final ProfileImgRepository profileImgRepository; // 유저가 소유하는 구조면 추가

    @Transactional
    public void deleteUserCascade(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 1) 자식 테이블들부터 삭제 (FK 때문에)
        // ※ 아래 deleteByUserId 메서드들은 Repository에 추가해야 함
        reviewRepository.deleteByUserId(userId);
        userWishRepository.deleteByUserId(userId);
        userGenreRepository.deleteByUserId(userId);
        userContentRepository.deleteByUserId(userId); // 있으면

        // 2) 마지막에 users 삭제
        userRepository.delete(user);
    }

    /**
     * 프론트에서 로그인 요청이 2번 들어와도
     * DB 중복 예외 없이 항상 같은 유저를 반환하도록 idempotent 하게 처리.
     */
    @Transactional
    public LoginResult loginWithGoogleIdToken(String idTokenString) {
        GoogleIdToken.Payload payload = verify(idTokenString);

        String email = payload.getEmail();
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Google payload email is empty");
        }
        email = email.trim().toLowerCase();

        String sub = payload.getSubject();
        if (sub == null || sub.isBlank()) {
            throw new IllegalArgumentException("Google payload subject(sub) is empty");
        }

        // ✅ 핵심: 중복 요청이 와도 예외 없이 insert or update (MySQL upsert)
        userRepository.upsertGoogleUser(email, sub);

        // ✅ 항상 하나만 조회되어야 함(email UNIQUE 전제)
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User should exist after upsert"));

        boolean isNewUser = (user.getNickname() == null);
        return new LoginResult(user, isNewUser);
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
