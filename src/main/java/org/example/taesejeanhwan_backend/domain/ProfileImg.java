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
@Table(name="profile_img")
public class ProfileImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private Long id;

    @Column(name = "img_url")
    private String imgUrl;

    @OneToMany(mappedBy = "profileImg")
    private List<User> users = new ArrayList<>();
}
