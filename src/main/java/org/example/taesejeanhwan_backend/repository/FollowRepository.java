package org.example.taesejeanhwan_backend.repository;

import org.example.taesejeanhwan_backend.domain.Follow;
import org.example.taesejeanhwan_backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByUser_id(Long user_id);

    boolean existsByAnotherUser(User user);

    List<Follow> findAllByUser_id(Long userId);

    List<Follow> findAllByAnotherUser_Id(Long userId);

    Follow findByUser_idAndAnotherUser_Id(Long userId, Long followId);

    boolean existsByUser_idAndAnotherUser_Id(Long userId, Long followId);
}
