package org.example.taesejeanhwan_backend.dto.user.request;

import lombok.Data;

@Data
public class UserRequestSetPreferenceContent {
    private Long content_id;
    private Float rating;
}
