package org.sopt.follow.repository;

import org.sopt.follow.domain.Follow;
import org.sopt.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    boolean existsByFollowerAndFollowee(User follower, User followee);

}