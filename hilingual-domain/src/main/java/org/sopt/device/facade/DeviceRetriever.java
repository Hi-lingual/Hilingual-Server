package org.sopt.device.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.device.domain.Device;
import org.sopt.device.exception.DeviceCoreErrorCode;
import org.sopt.device.exception.DeviceNotFoundException;
import org.sopt.device.repository.DeviceRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceRetriever {

    private final DeviceRepository deviceRepository;

    public Optional<Device> findByUserIdAndUuid(final long userId, final String uuid) {
        return deviceRepository.findByUser_IdAndUuid(userId, uuid);
    }

    public Optional<Device> findByUserIdAndDeviceName(final long userId, final String deviceName) {
        return deviceRepository.findByUser_IdAndDeviceName(userId, deviceName);
    }

    public List<Device> findAllByUserId(final long userId) {
        return deviceRepository.findAllByUser_Id(userId);
    }

    public Device findById(final long deviceId) {
        return deviceRepository.findById(deviceId)
                .orElseThrow(() -> new DeviceNotFoundException(DeviceCoreErrorCode.DEVICE_NOT_FOUND));
    }
}
