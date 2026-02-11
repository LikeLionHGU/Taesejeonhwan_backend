package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.domain.UserWish;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserWishRepository extends JpaRepository<UserWish, Long> {
    boolean existsByContentAndUser(Content content, User user);

    List<Long> findContent_idByUser(User user);

}
