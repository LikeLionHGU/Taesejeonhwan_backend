package org.example.taesejeanhwan_backend.domain;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Pattern(regexp = "^@?[a-zA-Z0-9]+$")
    @Size(min = 2, max = 8)
    @Column(length = 10, nullable = true)
    private String nickname;

    @Column(nullable = false, length = 50)
    private String googleSub;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "profile_img_id",
            referencedColumnName = "img_id",
            nullable = true
    )
    private ProfileImg profileImg;

}
