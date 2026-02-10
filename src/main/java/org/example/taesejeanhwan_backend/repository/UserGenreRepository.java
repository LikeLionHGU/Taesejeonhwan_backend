package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.UserGenre;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {
    void deleteByUser_Id(Long userId);
    List<UserGenre> findByUser_Id(Long userId);
}
