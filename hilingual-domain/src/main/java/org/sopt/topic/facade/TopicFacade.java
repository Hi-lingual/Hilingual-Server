package org.sopt.topic.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.topic.domian.Topic;
import org.sopt.usercalendar.exception.UserCalendarCoreErrorCode;
import org.sopt.usercalendar.exception.UserCalendarTopicNotFoundException;
import org.sopt.usercalendar.dto.UserCalendarTopicRes;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TopicFacade {

    private final TopicRetriever topicRetriever;

    public UserCalendarTopicRes findTopicByDate(LocalDate date) {
        final Topic topic = topicRetriever.findByDate(date)
                .orElseThrow(() -> new UserCalendarTopicNotFoundException(UserCalendarCoreErrorCode.TOPIC_NOT_FOUND));

        int remainingTime = calculateRemainingMinutesUntilDeadline(date);
        return UserCalendarTopicRes.of(topic.getTopicKor(), topic.getTopicEn(), remainingTime);
    }

    private int calculateRemainingMinutesUntilDeadline(LocalDate topicDate) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime deadline = topicDate.plusDays(2).atStartOfDay();
        long secondsRemaining = Duration.between(now, deadline).getSeconds();

        if (secondsRemaining <= 0) return 0;
        if (secondsRemaining <= 60) return 1;
        return (int) Math.ceil(secondsRemaining / 60.0);
    }

}
