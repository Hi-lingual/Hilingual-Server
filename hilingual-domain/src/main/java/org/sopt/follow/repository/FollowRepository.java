package org.sopt.follow.repository;

import org.sopt.follow.domain.Follow;
import org.sopt.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowee(User follower, User followee);

    // unfollow 이후 상태 계산용 (you -> me)
    boolean existsByFollowerIdAndFolloweeId(Long followerId, Long followeeId);

    // unfollow
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(
            "delete from Follow f " +
                    "where f.follower.id = :followerId " +
                    "and f.followee.id = :followeeId"
    )
    int deleteByFollowerIdAndFolloweeId(@Param("followerId") Long followerId,
                                        @Param("followeeId") Long followeeId);
}
