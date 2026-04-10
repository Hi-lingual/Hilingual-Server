package org.sopt.controller.test.dto.jwt;

import jakarta.validation.constraints.NotNull;
import org.sopt.type.UserRole;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.jwt.auth.domain.type.DeviceType;

public record IssueTokenRequest(
        @NotNull Long userId,
        @NotNull UserRole role,
        @NotNull AuthProvider provider,

        /* TODO [Soft Migration]
            uuid @NotNull하게 수정
         */
        String uuid,

        /* TODO [Soft Migration]
            구버전 앱 호환성을 위해 당분간 유지하며, 신버전 앱에서는 null로 들어옴
            기존 앱 Version 사용 유저가 없는 경우 완전 삭제 요망
        */
        String deviceName,
        DeviceType deviceType,
        String osType,
        String osVersion,
        String appVersion
) {}