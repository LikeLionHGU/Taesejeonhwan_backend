package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Data;
import org.example.taesejeanhwan_backend.domain.Follow;

@Data
@Builder
public class UserResponseFollowList {
    private Long user_id;
    private String nickname;
    private String profile_img;

    public static UserResponseFollowList following(Follow follow) {
        return UserResponseFollowList.builder()
                .user_id(follow.getAnotherUser().getId())
                .nickname(follow.getAnotherUser().getNickname())
                .profile_img(follow.getAnotherUser().getProfile_img())
                .build();

    }

    public static UserResponseFollowList follower(Follow follow) {
        return UserResponseFollowList.builder()
                .user_id(follow.getUser().getId())
                .nickname(follow.getUser().getNickname())
                .profile_img(follow.getUser().getProfile_img())
                .build();

    }
}
