package org.sopt.controller.follow.dto;

import org.sopt.controller.userprofile.dto.UserProfileSummaryRes;
import org.sopt.follow.dto.FolloweeIdAndIsFollowed;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public record FollowingListRes(
        List<FollowingRes> followingList
) {
    public static FollowingListRes of(final List<FolloweeIdAndIsFollowed> followings,
                                      final Map<Long, UserProfileSummaryRes> profilesMap) {
        final List<FollowingRes> list = followings.stream()
                .map(following -> {
                    UserProfileSummaryRes profile = profilesMap.get(following.getFolloweeId());
                    if (profile == null) {
                        return null;
                    }
                    return FollowingRes.of(profile, following.getIsFollowed());
                })
                .filter(Objects::nonNull)
                .toList();
        return new FollowingListRes(list);
    }

    record FollowingRes(
            Long userId,
            String nickname,
            String profileImg,
            boolean isFollowing,
            boolean isFollowed
    ) {
        static FollowingRes of(final UserProfileSummaryRes profile, final boolean isFollowed) {
            return new FollowingRes(
                    profile.userId(),
                    profile.nickname(),
                    profile.profileImg(),
                    true,
                    isFollowed
            );
        }
    }
}
