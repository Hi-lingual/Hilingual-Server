package org.sopt.follow.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.follow.dto.FollowRelation;
import org.sopt.follow.dto.FolloweeIdAndIsFollowed;
import org.sopt.follow.dto.FollowerIdAndIsFollowing;
import org.sopt.follow.repository.FollowRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowRetriever {

    private final FollowRepository followRepository;

    public List<FollowerIdAndIsFollowing> getFollowerListAndIsFollowing(Long userId) {
        return followRepository.findFollowerAndIsFollowingByUserId(userId);
    }

    public List<FolloweeIdAndIsFollowed> getFolloweeListAndIsFollowed(Long userId) {
        return followRepository.findFolloweeAndIsFollowedByUserId(userId);
    }

    public FollowRelation findFollowRelation(Long userId, Long targetUserId) {
        return followRepository.findFollowRelation(userId, targetUserId);
    }
}
