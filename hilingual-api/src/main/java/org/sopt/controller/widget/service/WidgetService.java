package org.sopt.controller.widget.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.widget.dto.res.WidgetStreakResponse;
import org.sopt.controller.widget.dto.res.WidgetTopicResponse;
import org.sopt.topic.domian.Topic;
import org.sopt.topic.facade.TopicFacade;
import org.sopt.usercalendar.domain.WriteStatus;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WidgetService {
    private final TopicFacade topicFacade;
    private final UserCalendarFacade userCalendarFacade;
    private final UserProfileFacade userProfileFacade;

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

    public WidgetStreakResponse getStreakWidget(final Long userId, final LocalDate date){
        final int streak = userProfileFacade.calculateStreak(userId, date);

        final LocalDate start = date.minusDays(4);
        final Map<LocalDate, WriteStatus> statusMap =
                userCalendarFacade.getStatusesByDateRange(userId, start, date);

        final List<WidgetStreakResponse.RecentDay> recentDays = new ArrayList<>();
        for (LocalDate d = start; !d.isAfter(date); d = d.plusDays(1)){
            final WriteStatus status = statusMap.getOrDefault(d, WriteStatus.NONE);
            recentDays.add(new WidgetStreakResponse.RecentDay(
                    d,
                    d.getDayOfWeek().name().substring(0, 3),    //"MONDAY" -> "MON"
                    isWritten(status)
            ));
        }
        return new WidgetStreakResponse(streak, recentDays);
    }
}