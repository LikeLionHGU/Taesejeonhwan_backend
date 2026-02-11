package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.UserContent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserContentRepository extends JpaRepository<UserContent, Long> {

    List<Content> findAllContent();
}
