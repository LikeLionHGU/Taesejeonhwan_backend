package org.example.taesejeanhwan_backend.dto.login;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.example.taesejeanhwan_backend.domain.User;

@Getter
@AllArgsConstructor
public class LoginResult {

    private final User user;
    private final boolean is_new_user;

}
