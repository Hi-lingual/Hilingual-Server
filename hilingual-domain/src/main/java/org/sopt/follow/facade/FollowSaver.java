package org.sopt.follow.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.follow.domain.Follow;
import org.sopt.follow.repository.FollowRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FollowSaver {

    private final FollowRepository followRepository;

    public Follow save(Follow follow) {
        return followRepository.save(follow);
    }
}