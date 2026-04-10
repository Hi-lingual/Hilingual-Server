package org.sopt.topic.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.topic.domian.Topic;
import org.sopt.usercalendar.domain.UserCalendar;
import org.sopt.usercalendar.domain.WriteStatus;
import org.sopt.usercalendar.exception.UserCalendarCoreErrorCode;
import org.sopt.usercalendar.exception.UserCalendarTopicNotFoundException;
import org.sopt.usercalendar.dto.UserCalendarTopicRes;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.springframework.stereotype.Component;

import java.time.*;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TopicFacade {

    private final TopicRetriever topicRetriever;
    private final UserCalendarFacade userCalendarFacade;

    public UserCalendarTopicRes findTopicByDate(Long userId, LocalDate date, ZoneId userZone) {
        Optional<UserCalendar> userCalendar = userCalendarFacade.findByUserIdAndDate(userId, date);
        if (userCalendar.isPresent() && userCalendar.get().getStatus() == WriteStatus.DELETED) {
            return UserCalendarTopicRes.of(" ", " ", -1);
        }

        final Topic topic = topicRetriever.findByDate(date)
                .orElseThrow(() -> new UserCalendarTopicNotFoundException(UserCalendarCoreErrorCode.TOPIC_NOT_FOUND));

        int remainingTime = calculateRemainingMinutesUntilDeadline(date, userZone);
        return UserCalendarTopicRes.of(topic.getTopicKor(), topic.getTopicEn(), remainingTime);
    }

    private int calculateRemainingMinutesUntilDeadline(LocalDate topicDate, ZoneId userZone) {
        final ZonedDateTime now = ZonedDateTime.now(userZone);
        final ZonedDateTime deadline = topicDate.plusDays(2).atStartOfDay(userZone);
        long secondsRemaining = Duration.between(now, deadline).getSeconds();

        if (secondsRemaining <= 0) return 0;
        if (secondsRemaining <= 60) return 1;
        return (int) Math.ceil(secondsRemaining / 60.0);
    }

}
