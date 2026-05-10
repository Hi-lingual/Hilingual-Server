package org.sopt.controller.notification.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.device.domain.Device;
import org.sopt.device.facade.DeviceFacade;
import org.sopt.firebase.FCMClient;
import org.sopt.firebase.dto.FCMMessageRequest;
import org.sopt.firebase.exception.FCMErrorCode;
import org.sopt.firebase.exception.FCMException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FcmNotificationServiceTest {

    @Mock
    private DeviceFacade deviceFacade;

    @Mock
    private FCMClient fcmClient;

    private FcmNotificationService fcmNotificationService;

    private static final Long TARGET_USER_ID = 1L;
    private static final Long DIARY_ID = 100L;
    private static final Long ACTOR_USER_ID = 2L;

    @BeforeEach
    void setUp() {
        fcmNotificationService = new FcmNotificationService(deviceFacade, Optional.of(fcmClient));
    }

    // ─── FCMClient 비활성 ────────────────────────────────────────────────────────

    @Test
    @DisplayName("FCMClient 비활성 상태이면 디바이스 조회도 하지 않고 종료된다")
    void sendLikeNotification_whenFcmClientAbsent_doesNothing() {
        FcmNotificationService disabledService =
                new FcmNotificationService(deviceFacade, Optional.empty());

        disabledService.sendLikeNotification(TARGET_USER_ID, DIARY_ID, "알림 내용");

        verifyNoInteractions(deviceFacade);
        verifyNoInteractions(fcmClient);
    }

    // ─── 토큰 null / blank 스킵 ──────────────────────────────────────────────────

    @Test
    @DisplayName("FCM 토큰이 null인 디바이스는 발송 건너뜀")
    void sendLikeNotification_whenTokenIsNull_skipsDevice() {
        Device device = mock(Device.class);
        when(device.getFcmToken()).thenReturn(null);
        when(deviceFacade.findAllByUserId(TARGET_USER_ID)).thenReturn(List.of(device));

        fcmNotificationService.sendLikeNotification(TARGET_USER_ID, DIARY_ID, "알림 내용");

        verifyNoInteractions(fcmClient);
    }

    @Test
    @DisplayName("FCM 토큰이 빈 문자열인 디바이스는 발송 건너뜀")
    void sendLikeNotification_whenTokenIsBlank_skipsDevice() {
        Device device = mock(Device.class);
        when(device.getFcmToken()).thenReturn("   ");
        when(deviceFacade.findAllByUserId(TARGET_USER_ID)).thenReturn(List.of(device));

        fcmNotificationService.sendLikeNotification(TARGET_USER_ID, DIARY_ID, "알림 내용");

        verifyNoInteractions(fcmClient);
    }

    // ─── 딥링크 포함 검증 ────────────────────────────────────────────────────────

    @Test
    @DisplayName("좋아요 알림 발송 시 diaryId 딥링크가 data.link에 포함된다")
    void sendLikeNotification_sendsWithDiaryDeepLink() {
        Device device = mock(Device.class);
        when(device.getFcmToken()).thenReturn("valid-token-123456");
        when(deviceFacade.findAllByUserId(TARGET_USER_ID)).thenReturn(List.of(device));

        fcmNotificationService.sendLikeNotification(TARGET_USER_ID, DIARY_ID, "OOO님이 공감했습니다.");

        verify(fcmClient).send(argThat(req ->
                "hilingual://app/home/diarydetail?diaryId=100"
                        .equals(req.data().get("link"))
        ));
    }

    @Test
    @DisplayName("팔로우 알림 발송 시 actorUserId 딥링크가 data.link에 포함된다")
    void sendFollowNotification_sendsWithFeedProfileDeepLink() {
        Device device = mock(Device.class);
        when(device.getFcmToken()).thenReturn("valid-token-123456");
        when(deviceFacade.findAllByUserId(TARGET_USER_ID)).thenReturn(List.of(device));

        fcmNotificationService.sendFollowNotification(TARGET_USER_ID, ACTOR_USER_ID, "OOO님이 팔로우했습니다.");

        verify(fcmClient).send(argThat(req ->
                "hilingual://app/home/feedprofile?userId=2"
                        .equals(req.data().get("link"))
        ));
    }

    // ─── 토큰 만료 처리 ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("INVALID_TOKEN 예외 발생 시 해당 디바이스 토큰을 clear한다")
    void sendLikeNotification_whenInvalidToken_clearsToken() {
        Device device = mock(Device.class);
        when(device.getFcmToken()).thenReturn("expired-token-123456");
        when(device.getDeviceId()).thenReturn(99L);
        when(deviceFacade.findAllByUserId(TARGET_USER_ID)).thenReturn(List.of(device));
        doThrow(new FCMException(FCMErrorCode.FCM_INVALID_TOKEN))
                .when(fcmClient).send(any(FCMMessageRequest.class));

        fcmNotificationService.sendLikeNotification(TARGET_USER_ID, DIARY_ID, "알림 내용");

        verify(deviceFacade).clearFcmToken(TARGET_USER_ID, 99L);
    }

    // ─── 발송 실패 시 예외 전파 차단 ──────────────────────────────────────────────

    @Test
    @DisplayName("FCM 발송 실패(FCM_SEND_FAILED) 시 예외가 상위로 전파되지 않는다")
    void sendLikeNotification_whenSendFailed_doesNotPropagate() {
        Device device = mock(Device.class);
        when(device.getFcmToken()).thenReturn("valid-token-123456");
        when(deviceFacade.findAllByUserId(TARGET_USER_ID)).thenReturn(List.of(device));
        doThrow(new FCMException(FCMErrorCode.FCM_SEND_FAILED))
                .when(fcmClient).send(any(FCMMessageRequest.class));

        Assertions.assertThatCode(
                () -> fcmNotificationService.sendLikeNotification(TARGET_USER_ID, DIARY_ID, "알림 내용")
        ).doesNotThrowAnyException();
    }

    // ─── 멀티 디바이스 ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("토큰이 있는 디바이스에만 발송되고, 없는 디바이스는 건너뛴다")
    void sendLikeNotification_multipleDevices_sendsOnlyToTokenOwners() {
        Device device1 = mock(Device.class);
        Device device2 = mock(Device.class);
        Device deviceNoToken = mock(Device.class);
        when(device1.getFcmToken()).thenReturn("token-device-1-aaa");
        when(device2.getFcmToken()).thenReturn("token-device-2-bbb");
        when(deviceNoToken.getFcmToken()).thenReturn(null);
        when(deviceFacade.findAllByUserId(TARGET_USER_ID))
                .thenReturn(List.of(device1, deviceNoToken, device2));

        fcmNotificationService.sendLikeNotification(TARGET_USER_ID, DIARY_ID, "알림 내용");

        verify(fcmClient, times(2)).send(any(FCMMessageRequest.class));
    }
}
