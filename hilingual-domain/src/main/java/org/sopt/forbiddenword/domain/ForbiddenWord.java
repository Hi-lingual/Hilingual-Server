package org.sopt.forbiddenword.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ForbiddenWord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String forbiddenWord;
}