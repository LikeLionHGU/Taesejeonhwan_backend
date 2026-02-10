package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.ContentGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentGenreRepository extends JpaRepository<ContentGenre, Long> {
    List<ContentGenre> findAllByContent_IdIn (List<Long> contentIds);

    boolean existsByContentIdAndGenreId(Long contentId, Long genreId);
}
