package org.example.taesejeanhwan_backend.domain;


import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "content_genres",
        uniqueConstraints = @UniqueConstraint(columnNames = {"content_id", "genre_id"})
)
@Getter
@NoArgsConstructor
public class ContentGenre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "content_id", nullable = false)
    private Content content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "genre_id", nullable = false)
    private Genre genre;

    public ContentGenre(Content content, Genre genre) {
        this.content = content;
        this.genre = genre;
    }
}
