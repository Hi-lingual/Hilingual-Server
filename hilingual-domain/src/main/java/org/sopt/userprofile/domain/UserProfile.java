package org.sopt.userprofile.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.user.domain.User;

import static org.sopt.userprofile.domain.UserProfileTableConstants.*;

@Entity
@Table(name = UserProfileTableConstants.TABLE_USER_PROFILE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserProfile extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_NICKNAME, nullable = false, length = 20, unique = true)
    private String nickname;

    @Column(name = COLUMN_PROFILE_IMG, length = 512)
    private String profileImg;

    @Column(name = COLUMN_TOTAL_DIARIES, nullable = false)
    private Integer totalDiaries = 0;

    @Column(name = COLUMN_STREAK, nullable = false)
    private Integer streak = 0;

    @Column(name = COLUMN_FOLLOWING_COUNT, nullable = false)
    private Integer followingCount = 0;

    @Column(name = COLUMN_FOLLOWER_COUNT, nullable = false)
    private Integer followerCount = 0;

    @Column(name = COLUMN_RECOVERY_CHANCE_COUNT, nullable = false)
    private Integer recoveryChanceCount = 3;

    @OneToOne
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
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
                3,
                user
        );
    }

    public void updateTotalDiaries(final int totalDiaries) {
        this.totalDiaries = totalDiaries;
    }

    public void updateStreak(final int streak) {
        this.streak = streak;
    }

    public void updateNickname(final String nickname) {
        this.nickname = nickname;
    }

    public void increaseFollowingCount() {
        this.followingCount = this.followingCount + 1;
    }

    public void decreaseFollowingCount() {
        this.followingCount = Math.max(0, this.followingCount - 1);
    }

    public void increaseFollowerCount() {
        this.followerCount = this.followerCount + 1;
    }

    public void decreaseFollowerCount() {
        this.followerCount = Math.max(0, this.followerCount - 1);
    }

    public void decreaseRecoveryChanceCount() {
        this.recoveryChanceCount = Math.max(0, this.recoveryChanceCount - 1);
    }

    public void resetRecoveryChanceCount() {
        this.recoveryChanceCount = 3;
    }
}
