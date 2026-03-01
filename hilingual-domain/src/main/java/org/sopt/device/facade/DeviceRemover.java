package org.sopt.device.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.device.repository.DeviceRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceRemover {

    private final DeviceRepository deviceRepository;

    public void deleteAllByUserId(final long userId) {
        deviceRepository.deleteAllByUser_Id(userId);
    }
}
