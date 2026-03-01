package org.sopt.device.dto;

import org.sopt.device.domain.type.DeviceType;

public record DeviceInfo(
        String timezone,
        String deviceUuid,
        String deviceName,
        DeviceType deviceType,
        String osType,
        String osVersion,
        String appVersion
) {
}
