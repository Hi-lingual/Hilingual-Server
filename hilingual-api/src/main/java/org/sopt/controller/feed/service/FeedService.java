package org.sopt.controller.feed.service;

import lombok.RequiredArgsConstructor;
import org.sopt.block.facade.BlockFacade;
import org.sopt.controller.feed.dto.FeedProfileRes;
import org.sopt.follow.dto.FollowRelation;
import org.sopt.follow.facade.FollowFacade;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedService {

    private final UserFacade userFacade;
    private final UserProfileFacade userProfileFacade;
    private final BlockFacade blockFacade;
    private final FollowFacade followFacade;

    @Transactional(readOnly = true)
    public FeedProfileRes getFeedProfile(Long userId, Long targetUserId) {
        // 존재하는 유저인지 확인
        userFacade.getUserById(userId);

        // isMine 검사
        boolean isMine = userId.equals(targetUserId);

        // 유저 프로필 조회
        UserProfile userProfile = userProfileFacade.getProfileByUserId(targetUserId);

        if (isMine) {
            return FeedProfileRes.from(userProfile, true, null, null, null);
        }

        // 내 프로필이 아닌 경우
        // 팔로우 관계 검사
        FollowRelation followRelation = followFacade.findFollowRelation(userId, targetUserId);

        // 차단 여부 검사
        boolean isBlocked = blockFacade.existsByBlockerIdAndBlockedId(userId, targetUserId);

        return FeedProfileRes.from(
                userProfile,
                false,
                followRelation.getIsFollowing(),
                followRelation.getIsFollowed(),
                isBlocked
        );
    }
}
