package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Content;
import org.example.taesejeanhwan_backend.domain.ContentGenre;
import org.example.taesejeanhwan_backend.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentGenreRepository extends JpaRepository<ContentGenre, Long> {
    List<ContentGenre> findAllByContent_IdIn (List<Long> contentIds);

    List<Long> findGenre_IdByContent(Content content);

    boolean existsByContentIdAndGenreId(Long contentId, Long genreId);
}
