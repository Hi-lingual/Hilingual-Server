package org.sopt.device.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.device.domain.Device;
import org.sopt.device.repository.DeviceRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DeviceRetriever {

    private final DeviceRepository deviceRepository;

    public Optional<Device> findByUserIdAndUuid(final long userId, final String uuid) {
        return deviceRepository.findByUser_IdAndUuid(userId, uuid);
    }
}
