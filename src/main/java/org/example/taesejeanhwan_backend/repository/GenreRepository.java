package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GenreRepository extends JpaRepository<Genre, Long> {
    Optional<Genre> findByTmdbId(Integer tmdbId);
}
