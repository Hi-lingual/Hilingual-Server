package org.sopt.follow.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.follow.repository.FollowRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FollowRemover {

    private final FollowRepository followRepository;

    @Transactional
    public int deleteByFollowerIdAndFolloweeId(Long followerId, Long followeeId){
        return followRepository.deleteByFollowerIdAndFolloweeId(followerId, followeeId);
    }
}