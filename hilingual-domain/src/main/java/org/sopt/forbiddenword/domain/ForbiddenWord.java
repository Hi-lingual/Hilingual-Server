package org.sopt.forbiddenword.domain;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Getter
@Entity
public class ForbiddenWord {
    @Id
    private Long id;

    @Column(length = 50)
    private String forbiddenWord;
}