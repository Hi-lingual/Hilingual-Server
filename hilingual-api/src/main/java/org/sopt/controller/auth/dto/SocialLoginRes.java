package org.sopt.controller.auth.dto;

import org.sopt.user.type.RegisterStatus;

public record SocialLoginRes(
        String accessToken,
        String refreshToken,
        Boolean registerStatus
) {
    public static SocialLoginRes of(String accessToken, String refreshToken, RegisterStatus registerStatus) {
        boolean registerStatusRes = registerStatus == RegisterStatus.PROFILE_COMPLETED;

        return new SocialLoginRes(
                accessToken,
                refreshToken,
                registerStatusRes
        );
    }
}
