package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByTmdbId(Integer tmdbId);

    Genre findGenreById(Long genre_id);
    Genre findByGenreName(String genreName);
}
