package org.sopt.controller.auth.dto;

import jakarta.validation.constraints.NotNull;
import org.sopt.device.dto.DeviceInfo;
import org.sopt.type.UserRole;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.jwt.auth.domain.type.DeviceType;
import org.springframework.util.StringUtils;

public record SocialLoginReq(
        @NotNull AuthProvider provider,
        @NotNull UserRole role,

        /* TODO [Soft Migration]
            uuid @NotNull하게 수정
         */
        String deviceUuid,

        // TODO 기존 앱Version 사용 유저 없는 경우 완전 삭제
        // @Deprecated - Soft Migration
        String deviceName,
        DeviceType deviceType,
        String osType,
        String osVersion,
        String appVersion
) {
    public boolean hasValidDeviceIdentifier() {
        return StringUtils.hasText(this.deviceUuid) || StringUtils.hasText(this.deviceName);
    }

    public DeviceInfo toDeviceInfo() {
        org.sopt.device.domain.type.DeviceType targetDeviceType = null;
        if (this.deviceType != null) {
            targetDeviceType = org.sopt.device.domain.type.DeviceType.valueOf(this.deviceType.name());
        }

        return new DeviceInfo(
                null,
                this.deviceUuid,
                this.deviceName,
                targetDeviceType,
                this.osType,
                this.osVersion,
                this.appVersion
        );
    }
}