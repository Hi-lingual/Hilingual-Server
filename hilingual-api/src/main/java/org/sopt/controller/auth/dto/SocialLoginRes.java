package org.sopt.controller.auth.dto;

import org.sopt.user.type.RegisterStatus;

public record SocialLoginRes(
        String accessToken,
        String refreshToken,
        Boolean registerStatus,
        Long userId
) {
    public static SocialLoginRes of(final String accessToken, final String refreshToken, final RegisterStatus registerStatus, final long userId) {
        boolean registerStatusRes = registerStatus == RegisterStatus.PROFILE_COMPLETED;

        return new SocialLoginRes(
                accessToken,
                refreshToken,
                registerStatusRes,
                userId
        );
    }
}
