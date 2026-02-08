package org.example.taesejeanhwan_backend.dto.user.request;

import lombok.Data;

import java.util.List;

@Data
public class UserRequestSetPreference {
    private Long user_id;
    private String nickname;
    private List<UserRequestSetPreferenceContent> user_contents;
}
