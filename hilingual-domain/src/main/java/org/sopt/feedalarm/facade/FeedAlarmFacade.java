package org.sopt.feedalarm.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.feedalarm.domain.FeedAlarm;
import org.sopt.feedalarm.event.FeedAlarmCreatedEvent;
import org.sopt.user.domain.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 피드 알림(좋아요, 팔로우) 관련 비즈니스 로직을 조합하는 Facade
 *
 * 알림 생성 후 ApplicationEventPublisher로 FeedAlarmCreatedEvent를 발행한다.
 * → hilingual-api의 FeedAlarmEventListener가 이벤트를 수신해서 SSE로 클라이언트에게 push한다.
 *
 * domain이 api를 직접 호출하지 않고 이벤트를 통해 분리한 이유:
 *   domain → api 직접 호출 시 순환 의존이 발생하기 때문
 */
@Component
@RequiredArgsConstructor
public class FeedAlarmFacade {

    private final FeedAlarmRetriever feedAlarmRetriever;
    private final FeedAlarmSaver feedAlarmSaver;
    private final FeedAlarmRemover feedAlarmRemover;

    /**
     * ApplicationEventPublisher: 스프링이 기본으로 제공하는 이벤트 발행 도구
     * 별도 의존성 추가 없이 주입받아 사용할 수 있다.
     */
    private final ApplicationEventPublisher eventPublisher;

    public void markAlarmAsRead(Long userId, Long alarmId) {
        feedAlarmRetriever.markAlarmAsRead(userId, alarmId);
    }

    public List<FeedAlarm> findLatestByUserId(final long userId) {
        return feedAlarmRetriever.findLatestByUserId(userId);
    }

    @Transactional
    public void createFollowAlarm(User targetUser, User actor) {
        final Long targetUserId = targetUser.getId();
        final Long actorId = actor.getId();

        // 이미 동일 알림 있으면 생성 X
        if (feedAlarmRetriever.existsRecentFollowAlarm(targetUserId, actorId)) {
            return;
        }

        final String title = actor.getUserProfile().getNickname() + "님이 당신을 팔로우했습니다.";
        final FeedAlarm alarm = FeedAlarm.createFollow(targetUser, actorId, title);

        try {
            feedAlarmSaver.save(alarm);
            targetUser.turnOnNotify();
            // 알림 저장 성공 → 이벤트 발행 → FeedAlarmEventListener가 SSE로 클라이언트에게 push
            eventPublisher.publishEvent(new FeedAlarmCreatedEvent(targetUserId));
        } catch (org.springframework.dao.DataIntegrityViolationException ignore) {
            // 동시성 환경에서 중복 insert 시 무시
            // 이 경우엔 알림이 이미 존재하므로 이벤트를 발행하지 않는다
            targetUser.turnOnNotify();
        }
    }

    @Transactional
    public void createLikeDiaryAlarm(Diary targetDiary, User actor) {
        final User targetUser = targetDiary.getUser();
        final Long targetUserId = targetDiary.getUser().getId();
        final Long actorId = actor.getId();

        // 자기 글이면 알림 생성 X
        if (targetUserId.equals(actorId)) {
            return;
        }

        // 최근 1분 내 동일 알림 존재 시 스킵
        if (feedAlarmRetriever.existsRecentLikeDiaryAlarm(targetUserId, actorId, targetDiary.getId())) {
            return;
        }

        String dateStr = targetDiary.getWrittenDate()
                .format(DateTimeFormatter.ofPattern("M월 d일"));
        String title = actor.getUserProfile().getNickname()
                + "님이 당신의 " + dateStr + " 일기에 공감했습니다.";

        final FeedAlarm alarm = FeedAlarm.createLikeDiary(
                targetDiary.getUser(),
                targetDiary.getId(),
                actorId,
                title
        );

        try {
            feedAlarmSaver.save(alarm);
            targetUser.turnOnNotify();
            // 알림 저장 성공 → 이벤트 발행 → FeedAlarmEventListener가 SSE로 클라이언트에게 push
            eventPublisher.publishEvent(new FeedAlarmCreatedEvent(targetUserId));
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // 동시성 환경에서 중복 insert 시 무시
            // 이 경우엔 알림이 이미 존재하므로 이벤트를 발행하지 않는다
            targetUser.turnOnNotify();
        }
    }

    public void deleteAllByUserId(final long userId) {
        feedAlarmRemover.deleteAllByUserId(userId);
    }

}
