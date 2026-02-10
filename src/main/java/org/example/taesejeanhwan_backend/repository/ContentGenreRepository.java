package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.ContentGenre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentGenreRepository extends JpaRepository<ContentGenre, Long> {
    boolean existsByContentIdAndGenreId(Long contentId, Long genreId);
}
