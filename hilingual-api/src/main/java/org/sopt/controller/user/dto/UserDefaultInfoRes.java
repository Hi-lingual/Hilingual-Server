package org.sopt.controller.user.dto;

import org.sopt.userprofile.domain.UserProfile;

public record UserDefaultInfoRes(
        String profileImg,
        String nickname,
        String provider
) {
    public static UserDefaultInfoRes from(final UserProfile profile, final String provider) {
        return new UserDefaultInfoRes(
                profile.getProfileImg(),
                profile.getNickname(),
                provider
        );
    }
}
