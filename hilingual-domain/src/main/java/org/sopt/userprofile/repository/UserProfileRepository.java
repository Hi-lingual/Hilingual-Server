package org.sopt.userprofile.repository;

import org.sopt.user.domain.User;
import org.sopt.userprofile.domain.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    boolean existsByNickname(String nickname);

    @Query("""
        SELECT up FROM UserProfile up
        JOIN FETCH up.user u
        WHERE u.id = :userId
    """)
    Optional<UserProfile> findByUserId(@Param("userId") Long userId);

    Optional<UserProfile> findByUser(User user);

    @Query("""
           SELECT up
           FROM UserProfile up
           WHERE up.user.id IN :userIds
           """)
    List<UserProfile> findByUserIds(@Param("userIds") List<Long> userIds);

    @Modifying
    @Query("UPDATE UserProfile up SET up.profileImg = :profileImg, up.updatedAt = :updatedAt WHERE up.user.id = :userId")
    int updateProfileImgByUserId(
            @Param("userId") Long userId,
            @Param("profileImg") String profileImg,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserProfile up SET up.followingCount = up.followingCount - 1 WHERE up.user.id = :userId AND up.followingCount > 0")
    int decrementFollowingCountByUserId(
            @Param("userId") Long userId,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    @Modifying(clearAutomatically = true)
    @Query("UPDATE UserProfile up SET up.followerCount = up.followerCount - 1 WHERE up.user.id = :userId AND up.followerCount > 0")
    int decrementFollowerCountByUserId(
            @Param("userId") Long userId,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    @Modifying
    @Query("UPDATE UserProfile up SET up.followerCount = up.followerCount - 1 WHERE up.user.id IN (SELECT f.followee.id FROM Follow f WHERE f.follower.id = :userId)")
    void decreaseFollowerCountOfFollowees(@Param("userId") Long userId);

    @Modifying
    @Query("UPDATE UserProfile up SET up.followingCount = up.followingCount - 1 WHERE up.user.id IN (SELECT f.follower.id FROM Follow f WHERE f.followee.id = :userId)")
    void decreaseFollowingCountOfFollowers(@Param("userId") Long userId);

    /*
     * 스트릭 스케줄러용 최적화 쿼리
     * 지정된 타임존 목록에 속해 있으면서, 스트릭이 0보다 큰 유저의 프로필만 User와 함께 페치 조인
     */
    @Query("""
        SELECT up FROM UserProfile up
        JOIN FETCH up.user u
        WHERE u.primaryTimezone IN :timezones
          AND up.streak > :streakThreshold
    """)
    List<UserProfile> findTargetsForStreakReset(
            @Param("timezones") Set<String> timezones,
            @Param("streakThreshold") int streakThreshold
    );

    /*
     * 월간 스트릭 부활권 초기화용 벌크 업데이트 쿼리
     * 지정된 타임존 목록에 속해 있으면서, 부활권이 3개 미만인 유저만 3개로 초기화
     */
    @Modifying(clearAutomatically = true)
    @Query("""
           UPDATE UserProfile up
           SET up.recoveryChanceCount = 3,
               up.updatedAt = :updatedAt
           WHERE up.recoveryChanceCount < 3
             AND up.user.primaryTimezone IN :timezones
           """)
    int bulkResetRecoveryChanceCount(
            @Param("timezones") Set<String> timezones,
            @Param("updatedAt") LocalDateTime updatedAt
    );

    long count();
}