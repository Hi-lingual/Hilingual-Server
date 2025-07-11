package org.hilingual.domain.recommend.core.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hilingual.common.domain.BaseTimeEntity;
import org.hilingual.domain.diary.core.domain.Diary;

import static org.hilingual.domain.recommend.core.domain.RecommendTableConstants.*;

@Entity
@Table(name = TABLE_RECOMMEND)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Recommend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_PHRASE, nullable = false)
    private String phrase;

    @Column(name = COLUMN_PHRASE_TYPE, nullable = false)
    private String phraseType;

    @Column(name = COLUMN_EXPLANATION, nullable = false)
    private String explanation;

    @Column(name = COLUMN_IS_MARKED, nullable = false)
    private Boolean isMarked;

    @Column(name = COLUMN_REASON, nullable = false)
    private String reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_DIARY_ID, nullable = false)
    private Diary diary;

    public static Recommend create(Diary diary, String phrase, String phraseType, String explanation, String reason) {
        return new Recommend(null, phrase, phraseType, explanation, false, reason, diary);
    }

}
