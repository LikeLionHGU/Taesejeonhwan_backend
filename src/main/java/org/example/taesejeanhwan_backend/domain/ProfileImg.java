package org.example.taesejeanhwan_backend.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="profileImg")
public class ProfileImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long img_id;

    private String img_url;

    @OneToMany(mappedBy = "profileImg")
    private List<User> users = new ArrayList<>();
}
