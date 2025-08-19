package org.sopt.controller.follow.dto;

import org.sopt.controller.userprofile.dto.UserProfileSummaryDtoRes;

public record FollowingDtoRes(
        Long userId,
        String nickname,
        String profileImg,
        boolean isFollowing,
        boolean isFollowed
){
    public static FollowingDtoRes of(UserProfileSummaryDtoRes profile, boolean isFollowed) {
        return new FollowingDtoRes(
                profile.userId(),
                profile.nickname(),
                profile.profileImg(),
                true,
                isFollowed
        );
    }
}
