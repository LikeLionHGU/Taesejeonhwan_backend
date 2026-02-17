package org.example.taesejeanhwan_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.dto.user.request.*;
import org.example.taesejeanhwan_backend.dto.user.response.*;
import org.example.taesejeanhwan_backend.service.ContentService;
import org.example.taesejeanhwan_backend.service.FollowService;
import org.example.taesejeanhwan_backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    public final UserService userService;
    public final FollowService followService;
    public final ContentService contentService;

    //프로필 이미지 설정
    @PostMapping("/profile-img")
    public UserResponseResult setProfileImage(@RequestBody UserRequestSetImage userRequestSetImage) {
        return userService.setProfileImage(userRequestSetImage);
    }

    //닉네임 설정
    @PostMapping("/nickname")
    public UserResponseResult setNickname(@RequestBody UserRequestSetNickname userRequestSetNickname) {
        return userService.setNickname(userRequestSetNickname);
    }

    //유저 취향 table 생성
    @PostMapping("/onboarding")
    public UserResponseResult setPreference(@RequestBody UserRequestSetPreference userRequestSetPreference) {
        return userService.setPreference(userRequestSetPreference);
    }

    //유저 팔로우
    @PostMapping("/follow")
    public UserResponseResult follow(@RequestBody UserRequestFollow userRequestFollow) {
        return followService.follow(userRequestFollow);
    }

    //닉네임 중복 확인
    @GetMapping("/check-nickname")
    public UserResponseCheckNickname checkNickname(@RequestBody UserRequestCheckNickname userRequestCheckNickname) {
        return userService.checkNickname(userRequestCheckNickname);
    }

    //콘텐츠 목록 100개 가져오기
    @GetMapping("/contents")
    public List<UserResponseContent> getContentsForSelection() {
        return contentService.getContentsForSelection();
    }

    //프로필 이미지 5개 가져오기
    @GetMapping("/profile-img")
    public List<UserResponseProfileImage> getAllProfileImages() {
        return userService.getAllProfileImages();
    }

    //프로필 조회
    @GetMapping("/profile/{user_id}")
    public UserResponseProfile getProfile(@PathVariable Long user_id) {
        return userService.getProfile(user_id);
    }

    //팔로워 리스트
    @GetMapping("/follows/{user_id}")
    public List<UserResponseFollowList> getFollowerList(@PathVariable Long user_id) {
        return followService.getFollowerList(user_id);
    }

    //팔로잉 리스트
    @GetMapping("/following/{user_id}")
    public List<UserResponseFollowList> getFollowingList(@PathVariable Long user_id) {
        return followService.getFollowingList(user_id);
    }

    //팔로우 취소
    @DeleteMapping("/follow/delete")
    public UserResponseResult deleteFollow(@RequestBody UserRequestFollow userRequestFollow) {
        return followService.deleteFollow(userRequestFollow);
    }


}
