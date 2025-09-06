package org.sopt.controller.userprofile.dto;

import org.sopt.userprofile.domain.UserProfile;

public record UserProfileSummaryRes(
        Long userId,
        String profileImg,
        String nickname
){
    public static UserProfileSummaryRes from(UserProfile profile, String profileImg) {
        return new UserProfileSummaryRes(
                profile.getUser().getId(),
                (profileImg != null) ? profileImg : " ",
                profile.getNickname()
        );
    }
}