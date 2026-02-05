package org.example.taesejeanhwan_backend.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "contents")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String poster;
    private Long year;



    @OneToMany(mappedBy = "content", fetch=FetchType.LAZY,
            cascade=CascadeType.ALL,
            orphanRemoval = true)
    private List<ContentGenre> contentGenre = new ArrayList<>();

    @OneToMany(mappedBy = "content", fetch=FetchType.LAZY,
            cascade=CascadeType.ALL,
            orphanRemoval = true)
    private List<Review> review = new ArrayList<>();

    @OneToMany(mappedBy = "content", fetch=FetchType.LAZY,
            cascade=CascadeType.ALL,
            orphanRemoval = true)
    private List<UserContent> userContent = new ArrayList<>();

    @OneToMany(mappedBy = "content", fetch=FetchType.LAZY,
            cascade=CascadeType.ALL,
            orphanRemoval = true)
    private List<UserWish> userWish = new ArrayList<>();

}
