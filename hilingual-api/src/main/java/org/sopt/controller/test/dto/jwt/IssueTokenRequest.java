package org.sopt.controller.test.dto.jwt;

import jakarta.validation.constraints.NotNull;
import org.sopt.jwt.auth.authentication.UserRole;
import org.sopt.jwt.auth.domain.AuthProvider;
import org.sopt.jwt.auth.domain.DeviceType;

public record IssueTokenRequest(
        @NotNull Long userId,
        @NotNull UserRole role,
        @NotNull AuthProvider provider,
        String deviceName,
        DeviceType deviceType,
        String osType,
        String osVersion,
        String appVersion
) {}