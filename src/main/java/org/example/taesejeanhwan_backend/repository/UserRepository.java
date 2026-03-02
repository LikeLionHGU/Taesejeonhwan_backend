package org.example.taesejeanhwan_backend.repository;
import org.example.taesejeanhwan_backend.domain.ProfileImg;
import org.example.taesejeanhwan_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository; // JPA가 제공하는 CRUD 기본 도구 상자(?)
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGoogleSub(String googleSub);

    Optional<User> findByEmail(String email);
    boolean existsByNickname(String nickname);
    @Modifying
    @Query(value = """
        INSERT INTO users (email, google_sub, nickname, profile_img_id)
        VALUES (:email, :sub, NULL, NULL)
        ON DUPLICATE KEY UPDATE
            google_sub = VALUES(google_sub)
        """, nativeQuery = true)
    int upsertGoogleUser(@Param("email") String email, @Param("sub") String sub);
    
    List<User> findByNicknameContaining(String keyword);
}
