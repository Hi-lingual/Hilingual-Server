package org.sopt.diaryfeedback.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.diary.domain.Diary;

@Entity
@Table(name = DiaryFeedbackTableConstants.TABLE_DIARY_FEEDBACK)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DiaryFeedback extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = DiaryFeedbackTableConstants.COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = DiaryFeedbackTableConstants.COLUMN_DIARY_ID, nullable = false)
    private Diary diary;

    @Column(name = DiaryFeedbackTableConstants.COLUMN_ORIGIN_PHRASE)
    private String originPhrase;

    @Column(name = DiaryFeedbackTableConstants.COLUMN_REWRITE_PHRASE, nullable = false)
    private String rewritePhrase;

    @Column(name = DiaryFeedbackTableConstants.COLUMN_EXPLANATION, nullable = false)
    private String explanation;

    @Column(name = DiaryFeedbackTableConstants.COLUMN_VERSION, nullable = false)
    private Integer version = 1;

    public static DiaryFeedback create(Diary diary, String originPhrase, String rewritePhrase, String explanation) {
        return new DiaryFeedback(null, diary, originPhrase, rewritePhrase, explanation, 1);
    }

}