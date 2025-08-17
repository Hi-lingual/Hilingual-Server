package org.sopt.controller.follow.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.follow.dto.FollowerDtoRes;
import org.sopt.controller.userprofile.dto.UserProfileSummaryDtoRes;
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
    public List<FollowerDtoRes> getFollowerList(Long userId) {
        userFacade.getUserById(userId);

        // 팔로워 ID, 팔로잉 여부 목록 조회
        List<FollowerIdAndIsFollowing> followers = followFacade.getFollowerListAndIsFollowing(userId);

        // 팔로워 ID 목록 추출
        List<Long> followerIds = followers.stream()
                .map(FollowerIdAndIsFollowing::getFollowerId)
                .toList();

        // 팔로워 프로필 정보 Map으로 변환
        Map<Long, UserProfileSummaryDtoRes> profilesMap = userProfileFacade.getProfilesByUserIds(followerIds)
                .stream()
                .map(UserProfileSummaryDtoRes::from)
                .collect(Collectors.toMap(UserProfileSummaryDtoRes::userId, Function.identity()));

        // 팔로워 목록과 프로필 정보 결합
        return followers.stream()
                .map(follower -> {
                    UserProfileSummaryDtoRes profile = profilesMap.get(follower.getFollowerId());
                    return FollowerDtoRes.of(profile, follower.getIsFollowing());
                })
                .toList();
    }
}

