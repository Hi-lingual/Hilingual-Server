package org.sopt.fcm.dto;

public record TargetDeviceDto(
        Long userId,
        Long deviceId,
        String fcmToken
) {}