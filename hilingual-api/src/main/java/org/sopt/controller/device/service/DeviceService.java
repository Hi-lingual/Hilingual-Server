package org.sopt.controller.device.service;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.device.dto.DeviceReq;
import org.sopt.controller.device.exception.DeviceApiErrorCode;
import org.sopt.controller.device.exception.MissingDeviceIdentifierException;

import org.sopt.device.dto.DeviceInfo;
import org.sopt.device.facade.DeviceFacade;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.ZoneId;


@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceFacade deviceFacade;
    private final UserFacade userFacade;

    @Transactional
    public void updateDeviceInfo(final long userId, final DeviceReq req, final ZoneId userZone) {
        if (!StringUtils.hasText(req.deviceUuid())) {
            throw new MissingDeviceIdentifierException(DeviceApiErrorCode.MISSING_DEVICE_IDENTIFIER);
        }

        DeviceInfo deviceInfo = req.toDeviceInfo();

        User user = userFacade.getUserById(userId);
        user.updatePrimaryTimezone(userZone.getId());

        deviceFacade.upsertDevice(userId, deviceInfo);
    }

}
