package org.sopt.controller.device.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateFcmTokenReq(
        @NotBlank(message = "디바이스 UUID는 필수입니다.")
        @Size(max = 255, message = "UUID는 255자를 초과할 수 없습니다.")
        String uuid,
        @NotBlank(message = "FCM 토큰은 필수입니다.")
        @Size(max = 512, message = "FCM 토큰은 512자를 초과할 수 없습니다.")
        String fcmToken
) {
}
