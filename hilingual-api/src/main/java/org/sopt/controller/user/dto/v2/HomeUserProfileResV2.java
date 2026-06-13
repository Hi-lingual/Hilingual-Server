package org.sopt.controller.user.dto.v2;

import org.sopt.userprofile.domain.UserProfile;

public record HomeUserProfileResV2(
        String nickname,
        String profileImg,
        int totalDiaries,
        int streak,
        Boolean newAlarm,
        int recoveryTickets
) {
    public static HomeUserProfileResV2 from(final UserProfile profile, final Boolean newAlarm, final String profileImg) {
        return new HomeUserProfileResV2(
                profile.getNickname(),
                (profileImg != null) ? profileImg : " ",
                profile.getTotalDiaries(),
                profile.getStreak(),
                newAlarm,
                profile.getRecoveryChanceCount()
        );
    }
}
