package org.example.taesejeanhwan_backend.controller;

import lombok.RequiredArgsConstructor;
import org.example.taesejeanhwan_backend.dto.user.request.*;
import org.example.taesejeanhwan_backend.dto.user.response.*;
import org.example.taesejeanhwan_backend.service.ContentService;
import org.example.taesejeanhwan_backend.service.FollowService;
import org.example.taesejeanhwan_backend.service.ProfileImageService;
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
    public final ProfileImageService profileImageService;

    @PostMapping("/profile-img")
    public UserResponseResult setProfileImage(@RequestBody UserRequestSetImage userRequestSetImage) {
        return userService.setProfileImage(userRequestSetImage);
    }

    @PostMapping("/nickname")
    public UserResponseResult setNickname(@RequestBody UserRequestSetNickname userRequestSetNickname) {
        return userService.setNickname(userRequestSetNickname);
    }

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
