package org.sopt.controller.follow.dto;

import org.sopt.controller.userprofile.dto.UserProfileSummaryRes;

public record FollowerDtoRes (
        Long userId,
        String nickname,
        String profileImg,
        boolean isFollowing,
        boolean isFollowed
){
    public static FollowerDtoRes of(UserProfileSummaryRes profile, boolean isFollowing) {
        return new FollowerDtoRes(
                profile.userId(),
                profile.nickname(),
                profile.profileImg(),
                isFollowing,
                true
        );
    }
}
