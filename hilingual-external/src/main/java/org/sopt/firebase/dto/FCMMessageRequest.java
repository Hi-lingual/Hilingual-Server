package org.sopt.firebase.dto;

import java.util.Map;

/**
 * FCM 발송 요청 DTO.
 * @param token  대상 디바이스의 FCM 토큰
 * @param title  푸시 알림 제목
 * @param body   푸시 알림 본문
 * @param data   클라이언트 앱에서 사용할 커스텀 데이터 (딥링크 등)
 */
public record FCMMessageRequest(
        String token,
        String title,
        String body,
        Map<String, String> data
) {
    public FCMMessageRequest {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("FCM token must not be blank");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("body must not be blank");
        }
        data = data != null ? Map.copyOf(data) : Map.of(); // 방어적 복사 — 외부 변경으로부터 보호
    }

    public static FCMMessageRequest of(String token, String title, String body) {
        return new FCMMessageRequest(token, title, body, Map.of());
    }

    public static FCMMessageRequest of(String token, String title, String body, Map<String, String> data) {
        return new FCMMessageRequest(token, title, body, data);
    }
}
