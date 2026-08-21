package org.sopt.controller.widget.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.widget.dto.res.WidgetTopicResponse;
import org.sopt.topic.domian.Topic;
import org.sopt.topic.facade.TopicFacade;
import org.sopt.usercalendar.domain.WriteStatus;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WidgetService {
    private final TopicFacade topicFacade;
    private final UserCalendarFacade userCalendarFacade;

    public WidgetTopicResponse getTopicWidget(final Long userId, final LocalDate date){
        final String topicEn = topicFacade.findByDate(date)
                .map(Topic::getTopicEn)
                .orElse(null);

        final Boolean isWrittenToday = (userId == null)
                ? null
                : isWritten(userCalendarFacade.getStatusByUserIdAndDate(userId, date));

        return new WidgetTopicResponse(date, topicEn, isWrittenToday);
    }

    private boolean isWritten(final WriteStatus status){
        return status == WriteStatus.WRITTEN || status == WriteStatus.RECOVERED;
    }
}