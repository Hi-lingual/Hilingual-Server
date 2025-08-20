package org.sopt.controller.follow.dto;

import org.sopt.controller.userprofile.dto.UserProfileSummaryRes;

public record FollowingDtoRes(
        Long userId,
        String nickname,
        String profileImg,
        boolean isFollowing,
        boolean isFollowed
){
    public static FollowingDtoRes of(UserProfileSummaryRes profile, boolean isFollowed) {
        return new FollowingDtoRes(
                profile.userId(),
                profile.nickname(),
                profile.profileImg(),
                true,
                isFollowed
        );
    }
}
