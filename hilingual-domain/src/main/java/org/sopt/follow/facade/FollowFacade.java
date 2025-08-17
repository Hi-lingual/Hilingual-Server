package org.sopt.follow.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.follow.dto.FollowerIdAndIsFollowing;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FollowFacade {

    private final FollowRetriever followRetriever;

    @Transactional(readOnly = true)
    public List<FollowerIdAndIsFollowing> getFollowerListAndIsFollowing(Long userId) {
        return followRetriever.getFollowerListAndIsFollowing(userId);
    }
}
