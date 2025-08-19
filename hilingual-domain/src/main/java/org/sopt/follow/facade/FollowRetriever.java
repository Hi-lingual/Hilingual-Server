package org.sopt.follow.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.follow.exception.FollowAlreadyExistsException;
import org.sopt.follow.exception.FollowCoreErrorCode;
import org.sopt.follow.repository.FollowRepository;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FollowRetriever {

    private final FollowRepository followRepository;

    @Transactional(readOnly = true)
    public void assertNotFollowing(User follower, User followee) {
        if (followRepository.existsByFollowerAndFollowee(follower, followee)) {
            throw new FollowAlreadyExistsException(FollowCoreErrorCode.FOLLOW_ALREADY_EXISTS);
        }
    }

    @Transactional(readOnly = true)
    public boolean existsByFollowerIdAndFolloweeId(Long aId, Long bId){
        return followRepository.existsByFollowerIdAndFolloweeId(aId, bId);
    }
}