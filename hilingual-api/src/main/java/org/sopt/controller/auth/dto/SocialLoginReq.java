package org.sopt.controller.auth.dto;

import jakarta.validation.constraints.NotNull;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.jwt.auth.domain.type.DeviceType;

public record SocialLoginReq(
        @NotNull AuthProvider provider,
        @NotNull UserRole role,

        /* TODO [Soft Migration]
            uuid @NotNull하게 수정
         */
        String uuid,

        // TODO 기존 앱Version 사용 유저 없는 경우 완전 삭제
        // @Deprecated - Soft Migration
        String deviceName,
        DeviceType deviceType,
        String osType,
        String osVersion,
        String appVersion
) {}