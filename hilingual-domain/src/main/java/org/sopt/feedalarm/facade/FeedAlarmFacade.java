package org.sopt.feedalarm.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedAlarmFacade {

    private final FeedAlarmRetriever feedAlarmRetriever;

    public void markAlarmAsRead(Long userId, Long alarmId) {
        feedAlarmRetriever.markAlarmAsRead(userId, alarmId);
    }

}