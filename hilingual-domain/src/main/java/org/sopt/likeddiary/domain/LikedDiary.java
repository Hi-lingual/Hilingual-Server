package org.sopt.likeddiary.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.diary.domain.Diary;
import org.sopt.user.domain.User;

import static org.sopt.likeddiary.domain.LikedDiaryTableConstants.*;

@Entity
@Table(
        name = TABLE_LIKED_DIARY,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = UK_USER_DIARY,
                        columnNames = {COLUMN_USER_ID, COLUMN_DIARY_ID}
                )
        }
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LikedDiary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_DIARY_ID, nullable = false)
    private Diary diary;

    public static LikedDiary create(User user, Diary diary) {
        return new LikedDiary(null, user, diary);
    }
}
