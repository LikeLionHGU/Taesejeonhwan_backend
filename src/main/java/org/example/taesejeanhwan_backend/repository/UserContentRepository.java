package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.domain.UserContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

@Repository
public interface UserContentRepository extends JpaRepository<UserContent, Long> {

    List<UserContent> findAllByUser(User user);
    List<Content> findAllContent();

    @EntityGraph(attributePaths = "content")
    List<UserContent> findByUserId(Long userId);
}
