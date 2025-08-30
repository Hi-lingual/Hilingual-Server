package org.sopt.follow.repository;

import org.sopt.follow.domain.Follow;
import org.sopt.follow.dto.FollowRelation;
import org.sopt.user.domain.User;
import org.sopt.follow.dto.FolloweeIdAndIsFollowed;
import org.sopt.follow.dto.FollowerIdAndIsFollowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    // 이미 팔로우 관계인지 (me -> you)
    boolean existsByFollowerAndFollowee(User follower, User followee);

    // 언팔로우 이후 상태 계산용 (you -> me)
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // 언팔로우
    @Modifying(flushAutomatically = true)
    @Query("""
           delete from Follow f
            where f.follower.id = :followerId
              and f.followee.id = :followeeId
           """)
    int deleteByFollowerIdAndFolloweeId(@Param("followerId") Long followerId,
                                        @Param("followeeId") Long followeeId);

    // 나를 팔로우하는 사람들 + 그들을 내가 팔로우 중인지 여부
    @Query("""
           SELECT f1.follower.id AS followerId,
                  CASE WHEN f2.id IS NOT NULL THEN true
                       ELSE false
                       END AS isFollowing
           FROM Follow f1
           LEFT JOIN Follow f2
                  ON f2.follower.id = :userId AND f2.followee.id = f1.follower.id
           WHERE f1.followee.id = :userId
           """)
    List<FollowerIdAndIsFollowing> findFollowerAndIsFollowingByUserId(@Param("userId") Long userId);

    // 내가 팔로우하는 사람들 + 그들이 나를 팔로우 중인지 여부
    @Query("""
           SELECT f1.followee.id AS followeeId,
                  CASE WHEN f2.id IS NOT NULL THEN true ELSE false END AS isFollowed
             FROM Follow f1
        LEFT JOIN Follow f2
               ON f2.followee.id = :userId
              AND f2.follower.id = f1.followee.id
            WHERE f1.follower.id = :userId
           """)
    List<FolloweeIdAndIsFollowed> findFolloweeAndIsFollowedByUserId(@Param("userId") Long userId);

    // 나와 대상자에 대해 isFollowed, isFollowing 여부 확인
    @Query("""
       SELECT
          (EXISTS(SELECT 1 FROM Follow f WHERE f.follower.id = :userId AND f.followee.id = :targetUserId)) AS isFollowing,
          (EXISTS(SELECT 1 FROM Follow f WHERE f.follower.id = :targetUserId AND f.followee.id = :userId)) AS isFollowed
       """)
    FollowRelation findFollowRelation(
            @Param("userId") Long userId,
            @Param("targetUserId") Long targetUserId
    );
}