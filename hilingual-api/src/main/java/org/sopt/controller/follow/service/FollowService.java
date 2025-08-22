package org.sopt.controller.follow.service;

import lombok.RequiredArgsConstructor;

import org.sopt.block.facade.BlockFacade;

import org.sopt.controller.follow.dto.NewFollowInfoRes;
import org.sopt.controller.follow.dto.FollowerDtoRes;
import org.sopt.controller.follow.dto.FollowerListDtoRes;
import org.sopt.controller.follow.dto.FollowingDtoRes;
import org.sopt.controller.follow.dto.FollowingListDtoRes;

import org.sopt.controller.follow.exception.FollowApiErrorCode;
import org.sopt.controller.follow.exception.SelfFollowNotAllowedException;

import org.sopt.controller.follow.dto.FollowerListRes;
import org.sopt.controller.follow.dto.FollowingListRes;
import org.sopt.controller.userprofile.dto.UserProfileSummaryRes;

import org.sopt.follow.dto.FolloweeIdAndIsFollowed;
import org.sopt.follow.dto.FollowerIdAndIsFollowing;
import org.sopt.follow.facade.FollowFacade;

import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;

import org.sopt.userprofile.facade.UserProfileFacade;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FollowService {

    private final UserFacade userFacade;
    private final FollowFacade followFacade;
    private final BlockFacade blockFacade;
    private final UserProfileFacade userProfileFacade;

    @Transactional
    public void follow(Long userId, Long targetUserId) {
        // 자기 자신 팔로우 불가능
        if (userId.equals(targetUserId)) {
            throw new SelfFollowNotAllowedException(FollowApiErrorCode.SELF_FOLLOW_NOT_ALLOWED);
        }
        User follower = userFacade.getUserById(userId);
        User followee = userFacade.getUserById(targetUserId);

        // A가 B를 차단했거나, B가 A를 차단했으면 팔로우 금지
        blockFacade.assertNotBlockedEitherDirection(follower, followee);
        // 이미 팔로우 했는지 체크
        followFacade.assertNotFollowing(follower, followee);

        followFacade.save(follower, followee);
    }

    @Transactional
    public NewFollowInfoRes unfollow(Long userId, Long targetUserId) {
        // 자기 자신 언팔로우 불가능
        if (userId.equals(targetUserId)) {
            throw new SelfFollowNotAllowedException(FollowApiErrorCode.SELF_UNFOLLOW_NOT_ALLOWED);
        }
        User me = userFacade.getUserById(userId);
        User you = userFacade.getUserById(targetUserId);

        followFacade.deleteIfExists(me, you);

        // 언팔 직후 me->you는 항상 false, you->me만 확인
        boolean followedBy = followFacade.isFollowing(you, me);
        return NewFollowInfoRes.of(followedBy);
    }

    @Transactional(readOnly = true)
    public FollowerListRes getFollowerList(Long userId) {
        userFacade.getUserById(userId);

        // 팔로워 ID, 팔로잉 여부 목록 조회
        final List<FollowerIdAndIsFollowing> followers = followFacade.getFollowerListAndIsFollowing(userId);

        // 팔로워 ID 목록 추출
        final List<Long> followerIds = followers.stream()
                .map(FollowerIdAndIsFollowing::getFollowerId)
                .toList();

        // 팔로워 프로필 정보 Map으로 변환
        final Map<Long, UserProfileSummaryRes> profilesMap = userProfileFacade.getProfilesByUserIds(followerIds)
                .stream()
                .map(UserProfileSummaryRes::from)
                .collect(Collectors.toMap(UserProfileSummaryRes::userId, Function.identity()));

        return FollowerListRes.of(followers, profilesMap);
    }

    @Transactional(readOnly = true)
    public FollowingListRes getFollowingList(Long userId) {
        userFacade.getUserById(userId);

        List<FolloweeIdAndIsFollowed> followings = followFacade.getFolloweeListAndIsFollowed(userId);

        List<Long> followeeIds = followings.stream()
                .map(FolloweeIdAndIsFollowed::getFolloweeId)
                .toList();

        Map<Long, UserProfileSummaryRes> profilesMap = userProfileFacade.getProfilesByUserIds(followeeIds)
                .stream()
                .map(UserProfileSummaryRes::from)
                .collect(Collectors.toMap(UserProfileSummaryRes::userId, Function.identity()));

        return FollowingListRes.of(followings, profilesMap);
    }

}