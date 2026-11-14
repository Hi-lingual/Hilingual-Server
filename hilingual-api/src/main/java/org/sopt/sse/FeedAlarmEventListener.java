package org.sopt.sse;

import lombok.RequiredArgsConstructor;
import org.sopt.feedalarm.event.FeedAlarmCreatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 피드 알림 생성 이벤트를 수신해서 SSE로 클라이언트에게 push하는 리스너
 *
 * 흐름:
 *   1. FeedAlarmFacade (domain) 에서 알림 저장 후 FeedAlarmCreatedEvent 발행
 *   2. 이 리스너가 이벤트를 수신
 *   3. SseEmitterManager.send()로 해당 유저에게 SSE 이벤트 전송
 *   4. 클라이언트는 이벤트를 받으면 알림 뱃지를 표시하고,
 *      기존 GET /api/v1/users/notifications 를 호출해서 알림 목록을 가져감
 */
@Component
@RequiredArgsConstructor
public class FeedAlarmEventListener {

    private final SseEmitterManager sseEmitterManager;

    /**
     * @EventListener: 스프링이 FeedAlarmCreatedEvent 발행을 감지하면 이 메서드를 자동으로 호출한다.
     *
     * 클라이언트에게 보내는 데이터: { "hasNewAlarm": true }
     *   → "새 알림이 생겼으니 알림 목록을 다시 조회해" 라는 신호만 보낸다.
     *   → 알림 상세 내용은 클라이언트가 기존 알림 목록 API를 호출해서 가져온다.
     */
    @EventListener
    public void handleFeedAlarmCreated(FeedAlarmCreatedEvent event) {
        sseEmitterManager.send(event.userId(), Map.of("hasNewAlarm", true));
    }
}
