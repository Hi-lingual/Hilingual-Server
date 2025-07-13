package org.hilingual.common.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Entity
public class Topic {
    @Id
    private LocalDate date;

    @Column(length = 255)
    private String topicEn;

    @Column(length = 255)
    private String topicKor;
}
