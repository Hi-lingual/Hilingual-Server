package org.sopt.sse;

import lombok.RequiredArgsConstructor;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * SSE(Server-Sent Events) 구독 요청을 처리하는 컨트롤러
 *
 * 역할: 클라이언트가 앱 시작 시 이 엔드포인트를 호출하면
 *       서버와 SSE 연결이 유지되어 실시간으로 알림 이벤트를 받을 수 있다.
 *
 * 흐름:
 *   1. 클라이언트 → GET /api/v1/sse/subscribe 호출
 *   2. SseEmitterManager.connect()로 emitter 생성 및 저장
 *   3. 이후 알림 발생 시 서버가 이 연결을 통해 이벤트를 push
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sse")
public class SseController {

    private final SseEmitterManager sseEmitterManager;

    /**
     * SSE 구독 엔드포인트
     *
     * produces = TEXT_EVENT_STREAM_VALUE:
     *   이 응답이 일반 HTTP 응답이 아닌 SSE 스트림임을 클라이언트에게 알려주는 헤더.
     *   이 설정이 없으면 클라이언트는 연결을 유지하지 않고 바로 끊어버린다.
     *
     * @param userId JWT 토큰에서 추출한 현재 로그인 유저의 ID
     * @return SseEmitter - Spring이 이 객체를 통해 클라이언트에게 이벤트를 전송한다
     */
    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@UserId Long userId) {
        return sseEmitterManager.connect(userId);
    }
}
