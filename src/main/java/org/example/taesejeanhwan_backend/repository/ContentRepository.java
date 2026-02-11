package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentRepository extends JpaRepository<Content, Long> {

    boolean existsByTitleAndYearAndPoster(String title, long year, String poster);


    Content findByContentId(Long contentId);
}

