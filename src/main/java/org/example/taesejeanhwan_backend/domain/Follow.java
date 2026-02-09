package org.example.taesejeanhwan_backend.domain;


import jakarta.persistence.*;
import lombok.*;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="followes")
public class Follow{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long another_user_id;

    @ManyToOne
    @JoinColumn(nullable=false)
    private User user;
}
