package org.example.taesejeanhwan_backend.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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

    @Pattern(regexp = "^[a-zA-Z0-9@]+$", message = "닉네임은 영어와 숫자만 입력 가능합니다.")
    @Size(min = 2, max = 10)
    @Column(length = 10, nullable = false)
    private String nickname;

    @Column(nullable = false, length = 50)
    private String googleSub;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

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

    public void updateOAuthProfile(String nickname) {
        this.nickname = nickname;
    }

}
