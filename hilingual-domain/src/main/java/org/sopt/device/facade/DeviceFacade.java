package org.sopt.device.facade;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.device.domain.Device;
import org.sopt.device.dto.DeviceInfo;
import org.sopt.device.exception.DeviceCoreErrorCode;
import org.sopt.device.exception.DeviceNotFoundException;
import org.sopt.device.exception.InvalidTimezoneFormatException;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.DateTimeException;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeviceFacade {

    private final DeviceRetriever deviceRetriever;
    private final DeviceRemover deviceRemover;
    private final DeviceSaver deviceSaver;
    private final UserFacade userFacade;

    @Transactional
    public void upsertDevice(final long userId, final DeviceInfo deviceInfo) {

        validateTimezone(deviceInfo.timezone());

        Optional<Device> optionalDevice = findExistingDevice(userId, deviceInfo);

        if (optionalDevice.isPresent()) {
            Device existingDevice = optionalDevice.get();
            existingDevice.updateDeviceInfo(
                    deviceInfo.timezone(),
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

    public List<Device> findAllByUserId(final long userId) {
        return deviceRetriever.findAllByUserId(userId);
    }

    public Device findByUserIdAndUuid(final long userId, final String uuid) {
        return deviceRetriever.findByUserIdAndUuid(userId, uuid)
                .orElseThrow(() -> new DeviceNotFoundException(DeviceCoreErrorCode.DEVICE_NOT_FOUND));
    }

    // userId 검증으로 타 사용자의 토큰 정리를 방지 (IDOR 방어)
    @Transactional
    public void clearFcmToken(final long userId, final long deviceId) {
        Device device = deviceRetriever.findById(deviceId);
        if (!device.getUser().getId().equals(userId)) {
            throw new DeviceNotFoundException(DeviceCoreErrorCode.DEVICE_NOT_FOUND);
        }
        device.clearFcmToken();
    }

    @Transactional
    public void deleteAllDevices(final long userId){
        deviceRemover.deleteAllByUserId(userId);
    }

    private void validateTimezone(String timezone) {
        try {
            if (StringUtils.hasText(timezone)) {
                ZoneId.of(timezone);
            }
        } catch (DateTimeException e) {
            throw new InvalidTimezoneFormatException(DeviceCoreErrorCode.INVALID_TIMEZONE_FORMAT);
        }
    }

    private Optional<Device> findExistingDevice(long userId, DeviceInfo deviceInfo) {
        // 신버전: UUID 존재하는 경우
        if (StringUtils.hasText(deviceInfo.deviceUuid())) {
            return deviceRetriever.findByUserIdAndUuid(userId, deviceInfo.deviceUuid());
        }

        // 구버전: UUID가 없는 경우
        else if (StringUtils.hasText(deviceInfo.deviceName())) {
            return deviceRetriever.findByUserIdAndDeviceName(userId, deviceInfo.deviceName());
        }

        return Optional.empty();
    }
}