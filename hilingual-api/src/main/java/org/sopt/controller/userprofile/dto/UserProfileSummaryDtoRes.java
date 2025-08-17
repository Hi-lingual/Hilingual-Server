package org.sopt.controller.userprofile.dto;

import org.sopt.userprofile.domain.UserProfile;

public record UserProfileSummaryDtoRes(
        Long userId,
        String profileImg,
        String nickname
){
    public static UserProfileSummaryDtoRes from(UserProfile profile) {
        return new UserProfileSummaryDtoRes(
                profile.getUser().getId(),
                profile.getProfileImg(),
                profile.getNickname()
        );
    }
}