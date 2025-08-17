package org.sopt.controller.follow.dto;

import org.sopt.controller.userprofile.dto.UserProfileSummaryDtoRes;

public record FollowerDtoRes (
        Long userId,
        String nickname,
        String profileImg,
        boolean isFollowing,
        boolean isFollowed
){
    public static FollowerDtoRes of(UserProfileSummaryDtoRes profile, boolean isFollowing, boolean isFollowed) {
        return new FollowerDtoRes(
                profile.userId(),
                profile.nickname(),
                profile.profileImg(),
                isFollowing,
                isFollowed
        );
    }
}
