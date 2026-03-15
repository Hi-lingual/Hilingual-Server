package org.sopt.controller.device.dto;

import org.sopt.controller.device.exception.DeviceApiErrorCode;
import org.sopt.controller.device.exception.InvalidDeviceTypeException;
import org.sopt.device.domain.type.DeviceType;
import org.sopt.device.dto.DeviceInfo;
import org.springframework.util.StringUtils;

public record DeviceReq(
        String timezone,
        String deviceUuid,
        String deviceName,
        String deviceType,
        String osType,
        String osVersion,
        String appVersion
) {
    public DeviceType toValidDeviceType() {
        if (!StringUtils.hasText(this.deviceType)) {
            throw new InvalidDeviceTypeException(DeviceApiErrorCode.INVALID_DEVICE_TYPE);
        }
        try {
            return DeviceType.valueOf(this.deviceType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidDeviceTypeException(DeviceApiErrorCode.INVALID_DEVICE_TYPE);
        }
    }

    public DeviceInfo toDeviceInfo() {
        return new DeviceInfo(
                this.timezone,
                this.deviceUuid,
                this.deviceName,
                this.toValidDeviceType(),
                this.osType,
                this.osVersion,
                this.appVersion
        );
    }
}
