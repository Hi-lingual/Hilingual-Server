package org.sopt.controller.follow.service;

import lombok.RequiredArgsConstructor;
import org.sopt.block.facade.BlockFacade;
import org.sopt.controller.follow.exception.FollowApiErrorCode;
import org.sopt.controller.follow.exception.SelfFollowNotAllowedException;
import org.sopt.follow.facade.FollowFacade;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final UserFacade userFacade;
    private final FollowFacade followFacade;
    private final BlockFacade blockFacade;

    @Transactional
    public void follow(Long followerId, Long followeeId) {
        // 자기 자신 팔로우 불가능
        if (followerId.equals(followeeId)) {
            throw new SelfFollowNotAllowedException(FollowApiErrorCode.SELF_FOLLOW_NOT_ALLOWED);
        }
        User follower = userFacade.getUserById(followerId);
        User followee = userFacade.getUserById(followeeId);

        // A가 B를 차단했거나, B가 A를 차단했으면 팔로우 금지
        blockFacade.assertNotBlockedEitherDirection(follower, followee);
        // 이미 팔로우 했는지 체크
        followFacade.assertNotFollowing(follower, followee);

        followFacade.save(follower, followee);
    }
}