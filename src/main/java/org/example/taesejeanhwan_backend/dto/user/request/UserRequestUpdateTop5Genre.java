package org.example.taesejeanhwan_backend.dto.user.request;

import lombok.Data;
import java.util.List;

@Data
public class UserRequestUpdateTop5Genre {
    private List<String> genre_name;
}