package org.hilingual.domain;

import jakarta.persistence.*;

@Entity
public class Recommend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phraseId;

    @Column(nullable = false)
    private String phrase;

    @Column(nullable = false)
    private String phraseType;

    @Column(name = "explanation", nullable = false)
    private String explain;

    @Column(nullable = false)
    private Boolean isMarked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;
}
