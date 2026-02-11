package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.Review;
import org.example.taesejeanhwan_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByContentAndUser(Content content, User user);

    List<Review> findByContent(Content content);
}
