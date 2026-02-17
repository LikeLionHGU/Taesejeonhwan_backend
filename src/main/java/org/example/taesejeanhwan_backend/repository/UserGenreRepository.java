package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.domain.UserGenre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGenreRepository extends JpaRepository<UserGenre, Long> {
    void deleteByUser_Id(Long userId);

    List<UserGenre> findByUser_Id(Long userId);

    List<UserGenre> findByUser(User reviewUser);

    void deleteByUser(User reviewUser);
}
