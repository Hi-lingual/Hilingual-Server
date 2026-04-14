package org.sopt.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sopt.firebase.dto.FCMMessageRequest;
import org.sopt.firebase.exception.FCMException;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FCMClientTest {

    @Mock
    private FirebaseMessaging firebaseMessaging;

    private FCMClient fcmClient;

    @BeforeEach
    void setUp() {
        fcmClient = new FCMClient(firebaseMessaging);
    }

    @Test
    @DisplayName("정상 발송 시 예외 없이 완료된다")
    void send_success() throws FirebaseMessagingException {
        when(firebaseMessaging.send(any(Message.class))).thenReturn("message-id-123");

        FCMMessageRequest request = FCMMessageRequest.of("valid-fcm-token-1234567890", "제목", "내용");

        assertThatCode(() -> fcmClient.send(request)).doesNotThrowAnyException();
        verify(firebaseMessaging).send(any(Message.class));
    }

    @Test
    @DisplayName("UNREGISTERED 토큰 시 FCMException(isInvalidToken=true) 발생")
    void send_unregisteredToken_throwsInvalidToken() throws FirebaseMessagingException {
        FirebaseMessagingException fme = mock(FirebaseMessagingException.class);
        when(fme.getMessagingErrorCode()).thenReturn(MessagingErrorCode.UNREGISTERED);
        when(firebaseMessaging.send(any(Message.class))).thenThrow(fme);

        FCMMessageRequest request = FCMMessageRequest.of("expired-token-1234567890", "제목", "내용");

        assertThatThrownBy(() -> fcmClient.send(request))
                .isInstanceOf(FCMException.class)
                .matches(e -> ((FCMException) e).isInvalidToken());
    }

    @Test
    @DisplayName("SENDER_ID_MISMATCH 시에도 무효 토큰으로 처리된다")
    void send_senderIdMismatch_throwsInvalidToken() throws FirebaseMessagingException {
        FirebaseMessagingException fme = mock(FirebaseMessagingException.class);
        when(fme.getMessagingErrorCode()).thenReturn(MessagingErrorCode.SENDER_ID_MISMATCH);
        when(firebaseMessaging.send(any(Message.class))).thenThrow(fme);

        FCMMessageRequest request = FCMMessageRequest.of("mismatch-token-1234567890", "제목", "내용");

        assertThatThrownBy(() -> fcmClient.send(request))
                .isInstanceOf(FCMException.class)
                .matches(e -> ((FCMException) e).isInvalidToken());
    }

    @Test
    @DisplayName("기타 Firebase 오류 시 FCMException(isInvalidToken=false) 발생")
    void send_otherError_throwsSendFailed() throws FirebaseMessagingException {
        FirebaseMessagingException fme = mock(FirebaseMessagingException.class);
        when(fme.getMessagingErrorCode()).thenReturn(MessagingErrorCode.INTERNAL);
        when(firebaseMessaging.send(any(Message.class))).thenThrow(fme);

        FCMMessageRequest request = FCMMessageRequest.of("valid-token-1234567890ab", "제목", "내용");

        assertThatThrownBy(() -> fcmClient.send(request))
                .isInstanceOf(FCMException.class)
                .matches(e -> !((FCMException) e).isInvalidToken());
    }

    @Test
    @DisplayName("예상치 못한 RuntimeException도 FCMException으로 래핑된다")
    void send_unexpectedException_wrappedAsFCMException() throws FirebaseMessagingException {
        when(firebaseMessaging.send(any(Message.class))).thenThrow(new RuntimeException("네트워크 오류"));

        FCMMessageRequest request = FCMMessageRequest.of("valid-token-1234567890ab", "제목", "내용");

        assertThatThrownBy(() -> fcmClient.send(request))
                .isInstanceOf(FCMException.class)
                .matches(e -> !((FCMException) e).isInvalidToken());
    }
}
