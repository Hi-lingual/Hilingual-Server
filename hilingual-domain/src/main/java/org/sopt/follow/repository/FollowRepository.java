package org.sopt.follow.repository;

import org.sopt.follow.domain.Follow;
import org.sopt.follow.dto.FollowRelation;
import org.sopt.follow.dto.FolloweeIdAndIsFollowed;
import org.sopt.follow.dto.FollowerIdAndIsFollowing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {

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

    @Query("""
           SELECT f1.followee.id AS followeeId,
                  CASE WHEN f2.id IS NOT NULL THEN true
                       ELSE false
                       END AS isFollowed
           FROM Follow f1
           LEFT JOIN Follow f2
                  ON f2.followee.id = :userId AND f2.follower.id = f1.followee.id
           WHERE f1.follower.id = :userId
           """)
    List<FolloweeIdAndIsFollowed> findFolloweeAndIsFollowedByUserId(@Param("userId") Long userId);

    // 나와 대상자에 대해 isFollowed, isFollowing 여부 확인
    @Query("""
       SELECT
          (SELECT COUNT(f) > 0 FROM Follow f
           WHERE f.follower.id = :userId AND f.followee.id = :targetUserId) AS isFollowing,
          (SELECT COUNT(f) > 0 FROM Follow f 
           WHERE f.follower.id = :targetUserId AND f.followee.id = :userId) AS isFollowed
       """)
    FollowRelation findFollowRelation(
            @Param("userId") Long userId,
            @Param("targetUserId") Long targetUserId
    );
}