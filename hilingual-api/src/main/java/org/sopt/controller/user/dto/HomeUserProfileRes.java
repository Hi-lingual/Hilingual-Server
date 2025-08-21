package org.sopt.controller.user.dto;

import org.sopt.userprofile.domain.UserProfile;

public record HomeUserProfileRes(
        String nickname,
        String profileImg,
        int totalDiaries,
        int streak,
        Boolean newAlarm
) {
    public static HomeUserProfileRes from(final UserProfile profile, final Boolean newAlarm) {
        return new HomeUserProfileRes(
                profile.getNickname(),
                profile.getProfileImg(),
                profile.getTotalDiaries(),
                profile.getStreak(),
                newAlarm
        );
    }
}
