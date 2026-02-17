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

    public UserResponseResult follow(UserRequestFollow request) {

        if (request.getFollow_id() == null || request.getUser_id() == null) {
            throw new IllegalArgumentException("Invalid request parameter");
        }

        User me = userRepository.findById(request.getUser_id())
                .orElseThrow(() -> new RuntimeException("User not found"));

        User target = userRepository.findById(request.getFollow_id())
                .orElseThrow(() -> new RuntimeException("Target user not found"));

        // 이미 팔로우했는지 체크
        if (followRepository.existsByUserAndAnotherUser(me, target)) {
            return UserResponseResult.builder()
                    .result("fail")
                    .build();
        }

        Follow follow = Follow.builder()
                .user(me)
                .anotherUser(target)
                .build();

        followRepository.save(follow);

        return UserResponseResult.builder()
                .result("success")
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

    public UserResponseResult deleteFollow(UserRequestFollow request) {

        if (request.getFollow_id() == null || request.getUser_id() == null) {
            throw new IllegalArgumentException("Ids cannot be null");
        }

        if (request.getFollow_id().equals(request.getUser_id())) {
            throw new IllegalArgumentException("You can't unfollow yourself");
        }

        Follow follow = followRepository
                .findByUser_idAndAnotherUser_Id(
                        request.getUser_id(),
                        request.getFollow_id()
                );

        if (follow == null) {
            throw new RuntimeException("Follow not found");
        }

        followRepository.delete(follow);

        return UserResponseResult.builder()
                .result("success")
                .build();
    }

}
