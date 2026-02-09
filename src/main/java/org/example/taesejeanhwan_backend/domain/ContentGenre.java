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
@Table(name = "contentGenres")
public class ContentGenre{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Content content;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Genre genre;
}
