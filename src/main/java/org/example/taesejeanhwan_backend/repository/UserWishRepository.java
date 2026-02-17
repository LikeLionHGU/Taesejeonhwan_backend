package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.domain.UserWish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserWishRepository extends JpaRepository<UserWish, Long> {
    boolean existsByContentAndUser(Content content, User user);

    List<Long> findContent_idByUser(User user);

    boolean existsByUserAndContent(User user, Content content);
}
