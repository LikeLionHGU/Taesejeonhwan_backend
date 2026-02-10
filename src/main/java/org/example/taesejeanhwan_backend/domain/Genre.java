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
@Table(name="genres")
public class Genre{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String genre_name;
    private Long tmdb_id;

    @OneToMany(mappedBy = "genre", fetch=FetchType.LAZY,
            cascade=CascadeType.ALL,
            orphanRemoval = true)
    private List<UserGenre> userGenre = new ArrayList<>();

    @OneToMany(mappedBy = "genre", fetch=FetchType.LAZY,
            cascade=CascadeType.ALL,
            orphanRemoval = true)
    private List<ContentGenre> contentGenre = new ArrayList<>();


}
