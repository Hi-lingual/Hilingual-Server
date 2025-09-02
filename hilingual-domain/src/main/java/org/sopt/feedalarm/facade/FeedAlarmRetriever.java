package org.sopt.feedalarm.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.feedalarm.domain.FeedAlarm;
import org.sopt.feedalarm.exception.FeedAlarmCoreErrorCode;
import org.sopt.feedalarm.exception.FeedAlarmNotFoundException;
import org.sopt.feedalarm.repository.FeedAlarmRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedAlarmRetriever {

    private final FeedAlarmRepository feedAlarmRepository;

    @Transactional
    public void markAlarmAsRead(Long userId, Long alarmId) {
        FeedAlarm alarm = feedAlarmRepository.findByIdAndUserId(alarmId, userId)
                .orElseThrow(() -> new FeedAlarmNotFoundException(FeedAlarmCoreErrorCode.FEED_ALARM_NOT_FOUND));
        alarm.markAsRead();
    }


    // 최근 N개의 알림 조회
    @Transactional(readOnly = true)
    public List<FeedAlarm> findLatestByUserId(final long userId) {
        return feedAlarmRepository.findTop500ByUserIdOrderByCreatedAtDesc(userId);
    }
}