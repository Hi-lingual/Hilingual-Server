package org.sopt.sse;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * SSE(Server-Sent Events) 연결을 관리하는 클래스
 *
 * 역할: userId와 SseEmitter(연결 객체)를 Map으로 관리한다.
 *   - 클라이언트가 구독 요청을 보내면 → connect()로 emitter를 생성하고 저장
 *   - 알림이 발생하면 → send()로 해당 userId의 emitter에 이벤트를 전송
 *
 * 이 클래스는 "연결 관리"만 담당한다.
 * 알림을 언제/어떤 내용으로 보낼지는 FeedAlarmEventListener에서 처리한다.
 */
@Component
@RequiredArgsConstructor
public class SseEmitterManager {

    /**
     * 현재 SSE로 연결된 유저들의 emitter를 저장하는 Map
     *
     * - key: userId (Long)
     * - value: SseEmitter (해당 유저와의 SSE 연결 객체)
     *
     * ConcurrentHashMap을 쓰는 이유:
     *   여러 요청이 동시에 들어올 때 일반 HashMap은 데이터가 꼬일 수 있다.
     *   ConcurrentHashMap은 멀티스레드 환경에서도 안전하게 동작한다.
     */
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    /**
     * 클라이언트가 SSE 구독 요청을 보낼 때 호출된다.
     * 새 SseEmitter를 생성하고, Map에 저장한 뒤 반환한다.
     *
     * @param userId 구독 요청을 보낸 유저의 ID
     * @return SseEmitter (Spring이 이 객체를 통해 클라이언트에게 이벤트를 전송한다)
     */
    public SseEmitter connect(Long userId) {
        // 타임아웃을 30분으로 설정한다.
        // 30분 동안 아무 이벤트가 없으면 연결이 자동으로 끊긴다.
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        emitters.put(userId, emitter);

        // 연결 직후 더미 데이터를 전송한다.
        // SSE는 데이터가 전혀 오지 않으면 nginx/로드밸런서 등 중간 인프라가
        // "응답 없음"으로 판단해 503을 반환하거나, 재연결 시 오류가 발생할 수 있다.
        // 따라서 "이 스트림 연결이 정상적으로 살아있다"는 신호를 즉시 보내야 한다.
        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            emitters.remove(userId);
        }

        // 연결이 정상적으로 완료(종료)되면 Map에서 제거한다.
        // remove(userId, emitter): 현재 Map에 저장된 emitter가 자기 자신일 때만 제거한다.
        // → 재연결로 새 emitter가 이미 등록된 경우, 새 emitter를 지우지 않기 위함이다.
        emitter.onCompletion(() -> emitters.remove(userId, emitter));

        // 타임아웃이 발생하면 Map에서 제거한다.
        emitter.onTimeout(() -> emitters.remove(userId, emitter));

        // 에러가 발생하면 Map에서 제거한다.
        // → 세 경우 모두 Map에서 제거해야 메모리 누수가 생기지 않는다.
        emitter.onError(e -> emitters.remove(userId, emitter));

        return emitter;
    }

    /**
     * 특정 유저에게 SSE 이벤트를 전송한다.
     * 알림이 생성될 때 FeedAlarmEventListener가 이 메서드를 호출한다.
     *
     * @param userId 이벤트를 받을 유저의 ID
     * @param data   클라이언트에게 전달할 데이터 (ex. 알림 내용)
     */
    public void send(Long userId, Object data) {
        SseEmitter emitter = emitters.get(userId);

        // 해당 유저가 현재 SSE로 연결되어 있지 않으면 전송하지 않는다.
        if (emitter == null) return;

        try {
            // 이벤트 이름을 "alarm"으로 지정해서 전송한다.
            // 클라이언트는 이벤트 이름으로 어떤 종류의 이벤트인지 구분할 수 있다.
            emitter.send(SseEmitter.event().name("alarm").data(data));
        } catch (IOException e) {
            // 전송 중 오류가 나면 연결이 끊긴 것으로 간주하고 Map에서 제거한다.
            emitters.remove(userId);
        }
    }
}
