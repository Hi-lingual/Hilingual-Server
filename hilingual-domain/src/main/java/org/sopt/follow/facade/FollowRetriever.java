package org.sopt.follow.facade;

import lombok.RequiredArgsConstructor;
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
}
