package org.sopt.fcm;

public record TargetDeviceDto(
        Long userId,
        Long deviceId,
        String fcmToken
) {}