package org.example.taesejeanhwan_backend.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.domain.Follow;
import org.example.taesejeanhwan_backend.domain.User;
import org.example.taesejeanhwan_backend.dto.user.request.UserRequestFollow;
import org.example.taesejeanhwan_backend.dto.user.response.UserResponseFollowList;
import org.example.taesejeanhwan_backend.dto.user.response.UserResponseResult;
import org.example.taesejeanhwan_backend.repository.FollowRepository;
import org.example.taesejeanhwan_backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {
    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public UserResponseResult follow(UserRequestFollow userRequestFollow) {
        if (userRequestFollow.getFollow_id() == null || userRequestFollow.getUser_id() == null) {
            throw new IllegalArgumentException("Invalid request parameter");
        }
        Follow follow = followRepository.findByUser_id(userRequestFollow.getUser_id());
        User user = userRepository.findById(userRequestFollow.getFollow_id()).orElseThrow(()->new RuntimeException("User not found"));
        follow.builder()
                .anotherUser(user)
                .build();
        followRepository.save(follow);
        if(followRepository.existsByAnotherUser(follow.getAnotherUser())) {
            return UserResponseResult.builder()
                    .result("success")
                    .build();
        }
        return UserResponseResult.builder()
                .result("fail")
                .build();
    }

    public List<UserResponseFollowList> getFollowerList(Long user_id) {
        if (user_id == null) {
            throw new IllegalArgumentException("Invalid request parameter");
        }
        List<Follow> followers = followRepository.findAllByAnotherUser_Id(user_id);
        return followers.stream()
                .map(UserResponseFollowList::follower)
                .toList();
    }

    public List<UserResponseFollowList> getFollowingList(Long user_id) {
        if (user_id == null) {
            throw new IllegalArgumentException("Invalid request parameter");
        }
        List<Follow> followings = followRepository.findAllByUser_id(user_id);
        return followings.stream()
                .map(UserResponseFollowList::following)
                .toList();
    }

    public UserResponseResult deleteFollow(UserRequestFollow userRequestFollow) {
        if (userRequestFollow.getFollow_id() == null||userRequestFollow.getUser_id() == null) {
            throw new IllegalArgumentException("Ids cannot be null");
        }

        if(userRequestFollow.getFollow_id().equals(userRequestFollow.getUser_id())) {
            throw new RuntimeException("You can't delete yourself");
        }
        Follow follow = followRepository.findByUser_idAndAnotherUser_Id(userRequestFollow.getUser_id(), userRequestFollow.getFollow_id());
        followRepository.delete(follow);
        if (followRepository.existsByUser_idAndAnotherUser_Id(userRequestFollow.getUser_id(), userRequestFollow.getFollow_id())) {
            return UserResponseResult.builder()
                    .result("fail")
                    .build();
        }
        return UserResponseResult.builder()
                .result("success")
                .build();
    }

}
