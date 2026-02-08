package org.example.taesejeanhwan_backend.dto.user.request;

import lombok.Data;

@Data
public class UserRequestFollow {
    private Long user_id;
    private Long follow_user_id;
}
