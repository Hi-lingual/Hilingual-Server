package org.hilingual.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Voca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vocaId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String phraseId;
}
