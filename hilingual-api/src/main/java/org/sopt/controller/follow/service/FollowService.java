package org.sopt.controller.follow.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.follow.dto.FollowerDtoRes;
import org.sopt.controller.follow.dto.FollowerListDtoRes;
import org.sopt.controller.follow.dto.FollowingDtoRes;
import org.sopt.controller.follow.dto.FollowingListDtoRes;
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
    public FollowerListDtoRes getFollowerList(Long userId) {
        userFacade.getUserById(userId);

        // 팔로워 ID, 팔로잉 여부 목록 조회
        List<FollowerIdAndIsFollowing> followers = followFacade.getFollowerListAndIsFollowing(userId);

        // 팔로워 ID 목록 추출
        List<Long> followerIds = followers.stream()
                .map(FollowerIdAndIsFollowing::getFollowerId)
                .toList();

        // 팔로워 프로필 정보 Map으로 변환
        Map<Long, UserProfileSummaryRes> profilesMap = userProfileFacade.getProfilesByUserIds(followerIds)
                .stream()
                .map(UserProfileSummaryRes::from)
                .collect(Collectors.toMap(UserProfileSummaryRes::userId, Function.identity()));

        // 팔로워 목록과 프로필 정보 결합
        return new FollowerListDtoRes(followers.stream()
                .map(follower -> {
                    UserProfileSummaryRes profile = profilesMap.get(follower.getFollowerId());
                    return FollowerDtoRes.of(profile, follower.getIsFollowing());
                })
                .toList()
        );
    }

    @Transactional(readOnly = true)
    public FollowingListDtoRes getFollowingList(Long userId) {
        userFacade.getUserById(userId);

        List<FolloweeIdAndIsFollowed> followings = followFacade.getFolloweeListAndIsFollowed(userId);

        List<Long> followeeIds = followings.stream()
                .map(FolloweeIdAndIsFollowed::getFolloweeId)
                .toList();

        Map<Long, UserProfileSummaryRes> profilesMap = userProfileFacade.getProfilesByUserIds(followeeIds)
                .stream()
                .map(UserProfileSummaryRes::from)
                .collect(Collectors.toMap(UserProfileSummaryRes::userId, Function.identity()));

        return new FollowingListDtoRes(followings.stream()
                .map(following -> {
                    UserProfileSummaryRes profile = profilesMap.get(following.getFolloweeId());
                    return FollowingDtoRes.of(profile, following.getIsFollowed());
                })
                .toList()
        );
    }
}

