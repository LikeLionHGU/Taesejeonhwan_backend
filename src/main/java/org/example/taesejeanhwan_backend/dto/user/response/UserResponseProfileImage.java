package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Data;
import org.example.taesejeanhwan_backend.domain.ProfileImg;

@Data
@Builder
public class UserResponseProfileImage {
    private String profile_img;

    public static UserResponseProfileImage from(ProfileImg profileImg) {
        return builder()
                .profile_img(profileImg.getImg_url())
                .build();
    }
}
