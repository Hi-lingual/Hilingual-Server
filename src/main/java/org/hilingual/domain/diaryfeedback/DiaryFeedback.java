package org.hilingual.domain.diaryfeedback;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hilingual.common.domain.BaseTimeEntity;
import org.hilingual.domain.diary.core.domain.Diary;

import static org.hilingual.domain.diaryfeedback.DiaryFeedbackTableConstants.*;

@Entity
@Table(name = TABLE_DIARY_FEEDBACK)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DiaryFeedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_DIARY_ID, nullable = false)
    private Diary diary;

    @Column(name = COLUMN_ORIGIN_PHRASE)
    private String originPhrase;

    @Column(name = COLUMN_REWRITE_PHRASE, nullable = false)
    private String rewritePhrase;

    @Column(name = COLUMN_EXPLANATION, nullable = false)
    private String explanation;
}