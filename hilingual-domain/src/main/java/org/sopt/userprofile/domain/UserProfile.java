package org.sopt.userprofile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.user.domain.User;

import static org.sopt.userprofile.domain.UserProfileTableConstants.DEFAULT_PROFILE_IMG;

@Entity
@Table(name = UserProfileTableConstants.TABLE_USER_PROFILE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = UserProfileTableConstants.COLUMN_ID)
    private Long id;

    @Column(name = UserProfileTableConstants.COLUMN_NICKNAME, nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(name = UserProfileTableConstants.COLUMN_PROFILE_IMG, nullable = false, length = 512)
    private String profileImg;

    @Column(name = UserProfileTableConstants.COLUMN_TOTAL_DIARIES, nullable = false)
    private Integer totalDiaries = 0;

    @Column(name = UserProfileTableConstants.COLUMN_STREAK, nullable = false)
    private Integer streak = 0;

    @Column(name = UserProfileTableConstants.COLUMN_FOLLOWING_COUNT, nullable = false)
    private Integer followingCount = 0;

    @Column(name = UserProfileTableConstants.COLUMN_FOLLOWER_COUNT, nullable = false)
    private Integer followerCount = 0;

    @OneToOne
    @JoinColumn(name = UserProfileTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    public static UserProfile create(User user, String nickname, String profileImg) {
        String finalImg = (profileImg != null && !profileImg.isBlank()) ? profileImg : DEFAULT_PROFILE_IMG;
        return new UserProfile(
                null,
                nickname,
                finalImg,
                0,
                0,
                0,
                0,
                user
        );
    }

    public void updateTotalDiaries(final int totalDiaries) {
        this.totalDiaries = totalDiaries;
    }

    public void updateStreak(final int streak) {
        this.streak = streak;
    }
}


