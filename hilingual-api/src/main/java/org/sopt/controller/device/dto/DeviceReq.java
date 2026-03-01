package org.sopt.controller.device.dto;

import org.sopt.device.domain.type.DeviceType;

public record DeviceReq(
        String timezone,
        String deviceUuid,
        String deviceName,
        DeviceType deviceType,
        String osType,
        String osVersion,
        String appVersion
) {
}
