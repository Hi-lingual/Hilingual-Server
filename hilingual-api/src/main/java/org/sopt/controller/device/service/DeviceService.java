package org.sopt.controller.device.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.device.dto.DeviceReq;
import org.sopt.controller.device.exception.DeviceApiErrorCode;
import org.sopt.controller.device.exception.MissingDeviceIdentifierException;

import org.sopt.device.dto.DeviceInfo;
import org.sopt.device.facade.DeviceFacade;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceFacade deviceFacade;

    @Transactional
    public void updateDeviceInfo(final long userId, final DeviceReq req) {
        if (!StringUtils.hasText(req.deviceUuid())) {
            throw new MissingDeviceIdentifierException(DeviceApiErrorCode.MISSING_DEVICE_IDENTIFIER);
        }

        DeviceInfo deviceInfo = req.toDeviceInfo();

        deviceFacade.upsertDevice(userId, deviceInfo);
    }

}
