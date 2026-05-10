package org.sopt.controller.device.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sopt.annotation.UserTimezone;
import org.sopt.controller.device.dto.DeviceReq;
import org.sopt.controller.device.dto.UpdateFcmTokenReq;
import org.sopt.controller.device.service.DeviceService;
import org.sopt.jwt.annotation.UserId;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.web.UserZone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
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
            @UserTimezone UserZone userZone,
            @RequestBody DeviceReq req
            ) {
        deviceService.updateDeviceInfo(userId, req, userZone.zoneId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/fcm-token")
    public ResponseEntity<Void> updateFcmToken(
            @UserId Long userId,
            @Valid @RequestBody UpdateFcmTokenReq req
    ) {
        deviceService.updateFcmToken(userId, req);
        return ResponseEntity.ok().build();
    }
}