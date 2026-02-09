package org.example.taesejeanhwan_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.dto.user.request.*;
import org.example.taesejeanhwan_backend.dto.user.response.*;

import org.example.taesejeanhwan_backend.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    public final UserService userService;
//    public final FollowService followService;
//    public final ContentService contentService;
//    public final ProfileImageService profileImageService;


    @Value("taesae")
    private String bucket;

    @Value("ap-northeast-2")
    private String region;

    //s3 이미지 파일 key = 1.png, 2.png ...
    @Value(".png")
    private String key;

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

//    //유저 취향 table 생성
    @PostMapping("/onboarding")
    public UserResponseSetPreference setPreference(@RequestBody UserRequestSetPreference userRequestSetPreference) {
        return userService.setPreference(userRequestSetPreference);
    }

    @PostMapping("/follow")
    public UserResponseResult follow(@RequestBody UserRequestFollow userRequestFollow) {
        return followService.follow(userRequestFollow);
    }

    @GetMapping("/check-nickname")
    public UserResponseCheckNickname checkNickname(@RequestBody UserRequestCheckNickname userRequestCheckNickname) {
        return userService.checkNickname(userRequestCheckNickname);
    }

    @GetMapping("/contents")
    public List<UserResponseContent> getContentsForSelection() {
        return contentService.getContentsForSelection();
    }

    @GetMapping("/profile-img")
    public List<UserResponseProfileImage> getAllProfileImages() {
        return profileImageService.getAllProfileImages();
    }

    @GetMapping("/profile/{user_id}")
    public UserResponseProfile getProfile(@PathVariable Long user_id) {
        return userService.getProfile(user_id);
    }

    @GetMapping("/follows/{user_id}")
    public List<UserResponseFollowList> getFollowerList(@PathVariable Long user_id) {
        return userService.getFollowerList(user_id);
    }

    @GetMapping("/following/{user_id}")
    public List<UserResponseFollowList> getFollowerList(@PathVariable Long user_id) {
        return userService.getFollowingList(user_id);
    }

    @DeleteMapping("/follow/delete")
    public UserResponseResult deleteFollow(@RequestBody UserRequestFollow userRequestFollow) {
        return followService.follow(userRequestFollow);
    }






}
