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

@Entity
@Table(name = VocaTableConstants.TABLE_VOCA)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Voca extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = VocaTableConstants.COLUMN_ID)
    private Long id;

    @Convert(converter = SavedRootConverter.class)
    @Column(name = VocaTableConstants.COLUMN_SAVED_ROOT, nullable = false)
    private SavedRoot savedRoot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = VocaTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = VocaTableConstants.COLUMN_RECOMMEND_ID, nullable = false, unique = true)
    private Recommend recommend;

    public Voca(User user, Recommend recommend) {
        this.user = user;
        this.recommend = recommend;
    }
}
