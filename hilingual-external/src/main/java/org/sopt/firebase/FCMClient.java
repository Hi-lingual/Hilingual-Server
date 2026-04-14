package org.sopt.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.firebase.dto.FCMMessageRequest;
import org.sopt.firebase.exception.FCMErrorCode;
import org.sopt.firebase.exception.FCMException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnBean(FirebaseMessaging.class)
public class FCMClient {

    private final FirebaseMessaging firebaseMessaging;

    public void send(FCMMessageRequest request) {
        Notification notification = Notification.builder()
                .setTitle(request.title())
                .setBody(request.body())
                .build();

        Message message = Message.builder()
                .setToken(request.token())
                .setNotification(notification)
                .putAllData(request.data())
                .build();

        try {
            String messageId = firebaseMessaging.send(message);
            log.info("FCM 발송 성공: messageId={}, token={}", messageId, maskToken(request.token()));
        } catch (FirebaseMessagingException e) {
            // 토큰 만료·삭제·프로젝트 불일치 → 호출측에서 토큰 정리하도록 INVALID_TOKEN으로 분류
            if (e.getMessagingErrorCode() == MessagingErrorCode.UNREGISTERED
                    || e.getMessagingErrorCode() == MessagingErrorCode.INVALID_ARGUMENT
                    || e.getMessagingErrorCode() == MessagingErrorCode.SENDER_ID_MISMATCH) {
                log.warn("FCM 유효하지 않은 토큰: token={}", maskToken(request.token()));
                throw new FCMException(FCMErrorCode.FCM_INVALID_TOKEN);
            }
            log.error("FCM 발송 실패: token={}, error={}", maskToken(request.token()), e.getMessage(), e);
            throw new FCMException(FCMErrorCode.FCM_SEND_FAILED);
        } catch (Exception e) {
            // 네트워크 타임아웃, SDK 내부 오류 등 예상치 못한 예외를 FCMException으로 래핑
            log.error("FCM 발송 중 예상치 못한 오류: token={}", maskToken(request.token()), e);
            throw new FCMException(FCMErrorCode.FCM_SEND_FAILED);
        }
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 10) return "***";
        return token.substring(0, 6) + "..." + token.substring(token.length() - 4);
    }
}
