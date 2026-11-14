package org.sopt.feedalarm.event;

/**
 * 피드 알림(좋아요, 팔로우)이 생성됐을 때 발행되는 스프링 이벤트
 *
 * 왜 이벤트를 쓰냐?
 *   알림 생성 로직은 hilingual-domain 모듈에 있고,
 *   SSE 전송 로직은 hilingual-api 모듈에 있다.
 *   domain이 api를 직접 호출하면 순환 의존이 생기므로,
 *   domain은 이벤트만 발행하고 api가 이벤트를 수신해서 SSE를 전송한다.
 *
 * 흐름:
 *   FeedAlarmFacade (domain) → 이벤트 발행
 *   FeedAlarmEventListener (api) → 이벤트 수신 → SseEmitterManager.send()
 *
 * @param userId 알림을 받을 유저의 ID
 */
public record FeedAlarmCreatedEvent(Long userId) {}
