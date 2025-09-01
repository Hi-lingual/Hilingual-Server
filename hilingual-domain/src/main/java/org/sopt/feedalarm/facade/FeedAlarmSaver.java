package org.sopt.feedalarm.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.feedalarm.domain.FeedAlarm;
import org.sopt.feedalarm.repository.FeedAlarmRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FeedAlarmSaver {

    private final FeedAlarmRepository feedAlarmRepository;

    @Transactional
    public FeedAlarm save(FeedAlarm alarm) {
        return feedAlarmRepository.save(alarm);
    }

}