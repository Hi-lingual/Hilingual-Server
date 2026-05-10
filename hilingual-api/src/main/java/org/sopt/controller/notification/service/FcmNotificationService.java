package org.sopt.controller.notification.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.device.domain.Device;
import org.sopt.device.facade.DeviceFacade;
import org.sopt.firebase.FCMClient;
import org.sopt.firebase.dto.FCMMessageRequest;
import org.sopt.firebase.exception.FCMException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmNotificationService {

    private static final String APP_NAME = "하이링구얼";
    private static final String DEEP_LINK_KEY = "link";
    private static final String DIARY_DETAIL_LINK = "hilingual://app/home/diarydetail?diaryId=%d";
    private static final String FEED_PROFILE_LINK = "hilingual://app/home/feedprofile?userId=%d";

    private final DeviceFacade deviceFacade;
    private final Optional<FCMClient> fcmClient;

    public void sendLikeNotification(Long targetUserId, Long diaryId, String body) {
        String deepLink = String.format(DIARY_DETAIL_LINK, diaryId);
        sendToAllDevices(targetUserId, body, deepLink);
    }

    public void sendFollowNotification(Long targetUserId, Long actorUserId, String body) {
        String deepLink = String.format(FEED_PROFILE_LINK, actorUserId);
        sendToAllDevices(targetUserId, body, deepLink);
    }

    private void sendToAllDevices(Long userId, String body, String deepLink) {
        if (fcmClient.isEmpty()) {
            log.debug("FCMClient 비활성화 상태 - 푸시 알림 미발송");
            return;
        }

        List<Device> devices = deviceFacade.findAllByUserId(userId);
        for (Device device : devices) {
            if (!StringUtils.hasText(device.getFcmToken())) {
                continue;
            }
            send(userId, device, body, deepLink);
        }
    }

    private void send(Long userId, Device device, String body, String deepLink) {
        try {
            FCMMessageRequest request = FCMMessageRequest.of(
                    device.getFcmToken(),
                    APP_NAME,
                    body,
                    Map.of(DEEP_LINK_KEY, deepLink)
            );
            fcmClient.get().send(request);
        } catch (FCMException e) {
            if (e.isInvalidToken()) {
                deviceFacade.clearFcmToken(userId, device.getDeviceId());
                log.warn("FCM 만료 토큰 clear: deviceId={}", device.getDeviceId());
            } else {
                log.error("FCM 발송 실패: deviceId={}", device.getDeviceId(), e);
            }
        }
    }
}
