package org.example.taesejeanhwan_backend.dto.user.request;

import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class UserRequestSetImage {
    private Long userId;
    private String profile_img;
}
