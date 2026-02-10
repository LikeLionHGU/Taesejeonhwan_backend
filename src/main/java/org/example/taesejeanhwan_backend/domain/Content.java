package org.example.taesejeanhwan_backend.domain;


import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(
        name = "contents",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"title", "year", "poster"})
        }
)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String overview;
    private String title;
    @Column(length = 500)
    private String poster;
    private int year;



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
