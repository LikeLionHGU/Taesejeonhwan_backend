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
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public UserResponseResult follow(UserRequestFollow userRequestFollow) {
        Follow follow = followRepository.findByUser_id(userRequestFollow.getUser_id());
        User user = userRepository.findByUser_id(userRequestFollow.getFollow_id());
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
        List<Follow> followers = followRepository.findAllByAnotherUser_Id(user_id);
        return followers.stream()
                .map(UserResponseFollowList::follower)
                .toList();
    }

    public List<UserResponseFollowList> getFollowingList(Long user_id) {
        List<Follow> followings = followRepository.findAllByUser_id(user_id);
        return followings.stream()
                .map(UserResponseFollowList::following)
                .toList();
    }

    public UserResponseResult deleteFollow(UserRequestFollow userRequestFollow) {
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
