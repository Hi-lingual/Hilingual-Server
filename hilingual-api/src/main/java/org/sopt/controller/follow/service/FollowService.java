package org.sopt.controller.follow.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.follow.dto.FollowerListRes;
import org.sopt.controller.follow.dto.FollowingListRes;
import org.sopt.controller.userprofile.dto.UserProfileSummaryRes;
import org.sopt.follow.dto.FolloweeIdAndIsFollowed;
import org.sopt.follow.dto.FollowerIdAndIsFollowing;
import org.sopt.follow.facade.FollowFacade;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserFacade userFacade;
    private final UserProfileFacade userProfileFacade;
    private final FollowFacade followFacade;

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

