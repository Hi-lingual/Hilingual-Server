package org.sopt.controller.device.api;

import lombok.RequiredArgsConstructor;
import org.sopt.annotation.UserTimezone;
import org.sopt.controller.device.dto.DeviceReq;
import org.sopt.controller.device.service.DeviceService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/device")
public class DeviceController {
    private final DeviceService deviceService;

    @PutMapping("")
    public ResponseEntity<Void> putDeviceInfo(
            @UserId Long userId,
            @UserTimezone ZoneId userZone,
            @RequestBody DeviceReq req
            ) {
        deviceService.updateDeviceInfo(userId, req, userZone);
        return ResponseEntity.ok().build();
    }
}