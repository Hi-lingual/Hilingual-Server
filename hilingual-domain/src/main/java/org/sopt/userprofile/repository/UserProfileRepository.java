package org.sopt.userprofile.repository;

import org.sopt.user.domain.User;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.dto.UserSearchProjection;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    /*
     * 검색 nickname에 해당하는 유저 리스트 검색
     * - 차단한 or 차단당한 유저는 검색되지 않음
     * - 앞단어가 일치하는 순서부터 중간 단어가 일치해도 표시
     * isFollowing, isFollowed 값을 함께 response로 내려줄 것
     */
    @Query("""
        SELECT
            up.user.id AS userId,
            up.profileImg AS profileImg,
            up.nickname AS nickname,
            CASE WHEN EXISTS (
                SELECT 1 FROM Follow f WHERE f.follower.id = :currentUserId AND f.followee.id = up.user.id
            ) THEN TRUE ELSE FALSE END AS isFollowing,
            CASE WHEN EXISTS (
                SELECT 1 FROM Follow f WHERE f.follower.id = up.user.id AND f.followee.id = :currentUserId
            ) THEN TRUE ELSE FALSE END AS isFollowed
        FROM UserProfile up
        WHERE up.user.id != :currentUserId
          AND up.nickname LIKE :keyword
          AND up.user.id NOT IN (
              SELECT b.blocked.id FROM Block b WHERE b.blocker.id = :currentUserId
          )
          AND up.user.id NOT IN (
              SELECT b.blocker.id FROM Block b WHERE b.blocked.id = :currentUserId
          )
        ORDER BY
            CASE WHEN up.nickname LIKE :startWithKeyword THEN 0 ELSE 1 END,
            up.nickname
        """)
    List<UserSearchProjection> searchUserListByKeyword(
            @Param("currentUserId") Long currentUserId,
            @Param("keyword") String keyword,
            @Param("startWithKeyword") String startWithKeyword
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
}
