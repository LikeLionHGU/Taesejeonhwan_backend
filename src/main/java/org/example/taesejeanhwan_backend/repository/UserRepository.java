package org.example.taesejeanhwan_backend.repository;
import org.example.taesejeanhwan_backend.domain.ProfileImg;
import org.example.taesejeanhwan_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository; // JPA가 제공하는 CRUD 기본 도구 상자(?)

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByGoogleSub(String googleSub);

    boolean existsByIdAndProfileImg(Long id, String profileImg);

    boolean existsByIdAndNickname(String nickname, Long userId);

    boolean existsByNickname(String nickname);

    User findByUser_id(Long id);
}
