package org.sopt.voca.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.voca.type.SavedRoot;
import org.sopt.voca.type.SavedRootConverter;
import java.time.LocalDate;

import static org.sopt.voca.domain.VocaTableConstants.*;

@Entity
@Table(
        name = TABLE_VOCA,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = UK_VOCA_USER_RECOMMEND,
                        columnNames = { COLUMN_USER_ID, COLUMN_RECOMMEND_ID }
                )
        }
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Voca extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Convert(converter = SavedRootConverter.class)
    @Column(name = COLUMN_SAVED_ROOT, nullable = false)
    private SavedRoot savedRoot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = COLUMN_RECOMMEND_ID, nullable = false)
    private Long recommendId;

    @Column(name = COLUMN_PHRASE, nullable = false)
    private String phrase;

    @Column(name = COLUMN_PHRASE_TYPE, nullable = false)
    private String phraseType;

    @Column(name = COLUMN_EXPLANATION, nullable = false)
    private String explanation;

    @Column(name = COLUMN_WRITTEN_FROM, nullable = false)
    private String writtenFrom;

    @Column(name = COLUMN_WRITTEN_DATE, nullable = true)
    private LocalDate writtenDate;

    @Column(name = COLUMN_IS_MEMORIZED, nullable = false)
    private boolean isMemorized;

    public static Voca fromMyDiary(User user, Recommend recommend, String writtenFrom) {
        return new Voca(
                null, SavedRoot.MY, user,
                recommend.getId(),
                recommend.getPhrase(),
                recommend.getPhraseType(),
                recommend.getExplanation(),
                writtenFrom,
                recommend.getDiary().getWrittenDate(),
                false
        );
    }

    public static Voca fromFeed(User user, Recommend recommend, String writtenFrom) {
        return new Voca(
                null, SavedRoot.FEED, user,
                recommend.getId(),
                recommend.getPhrase(),
                recommend.getPhraseType(),
                recommend.getExplanation(),
                writtenFrom,
                null,
                false
        );
    }

    public void updateMemorized(final boolean memorized) { this.isMemorized = memorized; }
}