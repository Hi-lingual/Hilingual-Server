package org.sopt.controller.follow.dto;

import org.sopt.controller.userprofile.dto.UserProfileSummaryRes;
import org.sopt.follow.dto.FollowerIdAndIsFollowing;

import java.util.List;
import java.util.Map;

public record FollowerListRes(
        List<FollowerRes> followerList
) {
    public static FollowerListRes of(final List<FollowerIdAndIsFollowing> followers,
                                     final Map<Long, UserProfileSummaryRes> profilesMap) {
        final List<FollowerRes> list = followers.stream()
                .map(follower -> {
                    UserProfileSummaryRes profile = profilesMap.get(follower.getFollowerId());
                    return FollowerRes.of(profile, follower.getIsFollowing());
                })
                .toList();
        return new FollowerListRes(list);
    }

    record FollowerRes(
            Long userId,
            String nickname,
            String profileImg,
            boolean isFollowing,
            boolean isFollowed
    ) {
        static FollowerRes of(final UserProfileSummaryRes profile, final boolean isFollowing) {
            return new FollowerRes(
                    profile.userId(),
                    profile.nickname(),
                    (profile.profileImg() != null) ? profile.profileImg() : " ",
                    isFollowing,
                    true
            );
        }
    }
}