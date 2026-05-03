package org.sopt.device.facade;


import lombok.RequiredArgsConstructor;
import org.sopt.device.domain.Device;
import org.sopt.device.repository.DeviceRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DeviceSaver {

    private final DeviceRepository deviceRepository;

    public void save(Device device) {
        deviceRepository.save(device);
    }
}
