package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.Review;
import org.example.taesejeanhwan_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    Review findByContentAndUser(Content content, User user);

    List<Review> findByContent(Content content);

    List<Review> findTop5ByUserAndContentInOrderByRatingDescCreateTimeDesc(User user, List<Content> contents);

    List<Review> findByUser(User reviewUser);
}
