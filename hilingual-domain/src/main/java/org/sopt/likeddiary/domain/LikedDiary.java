package org.sopt.likeddiary.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.user.domain.User;

@Entity
@Table(
        name = LikedDiaryTableConstants.TABLE_LIKED_DIARY,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {LikedDiaryTableConstants.COLUMN_USER_ID, LikedDiaryTableConstants.COLUMN_DIARY_ID}
                )
        }
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class LikedDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = LikedDiaryTableConstants.COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = LikedDiaryTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = LikedDiaryTableConstants.COLUMN_DIARY_ID, nullable = false)
    private Diary diary;

    public static LikedDiary create(User user, Diary diary) {
        return new LikedDiary(null, user, diary);
    }
}
