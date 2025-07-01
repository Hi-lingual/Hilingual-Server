package org.hilingual.diary.core.domain;

import jakarta.persistence.*;

@Entity
public class DiaryFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private Diary diary;

    private String originPhrase;

    @Column(nullable = false)
    private String rewritePhrase;

    @Column(name = "explanation", nullable = false)
    private String explanation;
}
