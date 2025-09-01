package org.sopt.controller.auth.dto;

import jakarta.validation.constraints.NotNull;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.jwt.auth.domain.type.DeviceType;

public record SocialLoginReq(
        @NotNull AuthProvider provider,
        @NotNull UserRole role,
        @NotNull String deviceName,
        @NotNull DeviceType deviceType,
        @NotNull String osType,
        @NotNull String osVersion,
        @NotNull String appVersion
) {}