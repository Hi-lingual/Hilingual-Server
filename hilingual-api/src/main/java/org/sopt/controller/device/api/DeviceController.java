package org.sopt.controller.device.api;

import lombok.RequiredArgsConstructor;
import org.sopt.annotation.UserTimezone;
import org.sopt.controller.device.dto.DeviceReq;
import org.sopt.controller.device.service.DeviceService;
import org.sopt.jwt.annotation.UserId;
import org.sopt.jwt.auth.domain.type.AuthProvider;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.web.UserZone;
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

    // TODO 삭제 (2026.05.03)
    private final UserFacade userFacade;

    @PutMapping("")
    public ResponseEntity<Void> putDeviceInfo(
            @UserId Long userId,
            @UserTimezone UserZone userZone,
            @RequestBody DeviceReq req
            ) {

        // TODO 삭제 (2026.05.03)
        User user = userFacade.getUserById(userId);
        if(user.getProvider().equals("APPLE")) {
            return ResponseEntity.ok().build();
        }

        deviceService.updateDeviceInfo(userId, req, userZone.zoneId());
        return ResponseEntity.ok().build();
    }
}