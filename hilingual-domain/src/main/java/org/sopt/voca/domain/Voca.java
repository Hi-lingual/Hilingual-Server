package org.sopt.voca.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.voca.type.SavedRoot;
import org.sopt.voca.type.SavedRootConverter;

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
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_RECOMMEND_ID, nullable = false)
    private Recommend recommend;

    public static Voca fromMyDiary(User user, Recommend recommend) {
        return new Voca(null, SavedRoot.MY, user, recommend);
    }

    public static Voca fromFeed(User user, Recommend recommend) {
        return new Voca(null, SavedRoot.FEED, user, recommend);
    }

}
