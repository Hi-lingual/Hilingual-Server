package org.sopt.controller.device.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.device.dto.DeviceReq;
import org.sopt.controller.device.service.DeviceService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/device")
public class DeviceController {
    private final DeviceService deviceService;

    @PutMapping("")
    public ResponseEntity<Void> putDeviceInfo(
            @UserId Long userId,
            @RequestBody DeviceReq req
            ) {
        deviceService.updateDeviceInfo(userId, req);
        return ResponseEntity.ok().build();
    }
}