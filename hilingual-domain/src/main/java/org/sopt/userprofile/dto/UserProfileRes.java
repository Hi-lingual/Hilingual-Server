package org.sopt.userprofile.dto;

import org.sopt.userprofile.domain.UserProfile;

public record UserProfileRes(
        String nickname,
        String profileImg,
        int totalDiaries,
        int streak
) {
    public static UserProfileRes from(final UserProfile profile) {
        return new UserProfileRes(
                profile.getNickname(),
                profile.getProfileImg(),
                profile.getTotalDiaries(),
                profile.getStreak()
        );
    }
}
