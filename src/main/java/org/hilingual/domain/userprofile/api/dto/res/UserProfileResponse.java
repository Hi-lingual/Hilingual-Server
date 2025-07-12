package org.hilingual.domain.userprofile.api.dto.res;

import org.hilingual.domain.userprofile.core.domain.UserProfile;

public record UserProfileResponse(
        String nickname,
        String profileImg,
        int totalDiaries,
        int streak
) {
    public static UserProfileResponse from(final UserProfile profile) {
        return new UserProfileResponse(
                profile.getNickname(),
                profile.getProfileImg(),
                profile.getTotalDiaries(),
                profile.getStreak()
        );
    }
}
