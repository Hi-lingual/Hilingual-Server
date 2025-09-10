package org.sopt.feedalarm.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.feedalarm.repository.FeedAlarmRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FeedAlarmRemover {
    
    private final FeedAlarmRepository feedAlarmRepository;
    
    public void deleteAllByUserId(final long userId) {
        feedAlarmRepository.deleteAllByUserId(userId);
    }
}
