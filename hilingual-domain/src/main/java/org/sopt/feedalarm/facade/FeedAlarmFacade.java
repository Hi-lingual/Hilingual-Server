package org.sopt.feedalarm.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.feedalarm.domain.FeedAlarm;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedAlarmFacade {

    private final FeedAlarmRetriever feedAlarmRetriever;
    private final FeedAlarmSaver feedAlarmSaver;
    private final FeedAlarmRemover feedAlarmRemover;

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
        } catch (org.springframework.dao.DataIntegrityViolationException ignore) {
            // 동시성 환경에서 중복 insert 시 무시
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
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // 동시성 환경에서 중복 insert 시 무시
            targetUser.turnOnNotify();

        }
    }

    public void deleteAllByUserId(final long userId) {
        feedAlarmRemover.deleteAllByUserId(userId);
    }

}