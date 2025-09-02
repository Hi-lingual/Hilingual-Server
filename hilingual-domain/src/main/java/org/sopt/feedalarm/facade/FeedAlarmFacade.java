package org.sopt.feedalarm.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.feedalarm.domain.FeedAlarm;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedAlarmFacade {

    private final FeedAlarmRetriever feedAlarmRetriever;
    private final FeedAlarmSaver feedAlarmSaver;

    public void markAlarmAsRead(Long userId, Long alarmId) {
        feedAlarmRetriever.markAlarmAsRead(userId, alarmId);
    }

    public List<FeedAlarm> findLatestByUserId(final long userId) {
        return feedAlarmRetriever.findLatestByUserId(userId);
    }

    @Transactional
    public void createFollowAlarm(User targetUser, User actor) {
        FeedAlarm alarm = FeedAlarm.createFollowUser(
                targetUser,
                actor.getId(),       
                actor.getUserProfile().getNickname() + "님이 당신을 팔로우했습니다."
        );
        feedAlarmSaver.save(alarm);
    }

}