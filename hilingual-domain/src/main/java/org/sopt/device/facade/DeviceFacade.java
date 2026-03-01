package org.sopt.device.facade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sopt.device.domain.Device;
import org.sopt.device.dto.DeviceInfo;
import org.sopt.device.exception.DeviceCoreErrorCode;
import org.sopt.device.exception.InvalidTimezoneFormatException;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceFacade {

    private final DeviceRetriever deviceRetriever;
    private final DeviceRemover deviceRemover;
    private final DeviceSaver deviceSaver;
    private final UserFacade userFacade;

    public Optional<Device> findByUserIdAndUuid(final long userId, final String uuid) {
        return deviceRetriever.findByUserIdAndUuid(userId, uuid);
    }

    @Transactional
    public void upsertDevice(final long userId, final DeviceInfo deviceInfo) {
        try {
            if (StringUtils.hasText(deviceInfo.timezone())) {
                ZoneId.of(deviceInfo.timezone());
            }
        } catch (DateTimeException e) {
            throw new InvalidTimezoneFormatException(DeviceCoreErrorCode.INVALID_TIMEZONE_FORMAT);
        }

        Optional<Device> optionalDevice = deviceRetriever.findByUserIdAndUuid(userId, deviceInfo.deviceUuid());

        if (optionalDevice.isPresent()) {
            Device existingDevice = optionalDevice.get();
            existingDevice.updateDeviceInfo(
                    deviceInfo.timezone(),
                    deviceInfo.deviceUuid(),
                    deviceInfo.osVersion(),
                    deviceInfo.appVersion()
            );
        } else {
            User user = userFacade.getUserById(userId);
            Device newDevice = Device.create(
                    user,
                    deviceInfo.deviceUuid(),
                    deviceInfo.timezone(),
                    deviceInfo.deviceName(),
                    deviceInfo.deviceType(),
                    deviceInfo.osType(),
                    deviceInfo.osVersion(),
                    deviceInfo.appVersion()
            );

            deviceSaver.save(newDevice);
        }
    }

    @Transactional
    public void deleteAllDevices(final long userId){
        deviceRemover.deleteAllByUserId(userId);
    }
}