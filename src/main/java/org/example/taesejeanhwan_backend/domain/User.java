package org.example.taesejeanhwan_backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String nickname;

    private String profile_img;

    @OneToMany(mappedBy = "user")
    private List<Follow> follows = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Review> reviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserWish> userWishes = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserContent> userContents = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserGenre> userGenres = new ArrayList<>();

    public void updateOAuthProfile(String name) {
        this.name = name;
    }

    public void connectGoogle(String googleSub) {
        this.googleSub = googleSub;
    }
}
