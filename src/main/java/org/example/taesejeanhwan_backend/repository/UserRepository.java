package org.example.taesejeanhwan_backend.repository;
import org.example.taesejeanhwan_backend.domain.ProfileImg;
import org.example.taesejeanhwan_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository; // JPA가 제공하는 CRUD 기본 도구 상자(?)
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByGoogleSub(String googleSub);

    boolean existsByIdAndProfileImg(Long id, ProfileImg profileImg);

    boolean existsByIdAndNickname(Long userId, String nickname);

    boolean existsByNickname(String nickname);

    User findByNickname(String keyword);
}
