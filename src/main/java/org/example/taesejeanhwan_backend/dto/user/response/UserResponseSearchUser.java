package org.example.taesejeanhwan_backend.dto.user.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserResponseSearchUser {
    private String result;
    private List<UserResponseSearchUserResult> results;
}
