package org.sopt.controller.usercalendar.service;

import lombok.RequiredArgsConstructor;
import org.sopt.aws.s3.utils.S3UrlResolver;
import org.sopt.controller.usercalendar.exception.FutureDateNotAllowedException;
import org.sopt.controller.usercalendar.exception.UserCalendarApiErrorCode;
import org.sopt.topic.facade.TopicFacade;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.usercalendar.dto.UserCalendarDiarySummaryRes;
import org.sopt.usercalendar.dto.UserCalendarMonthlyRes;
import org.sopt.usercalendar.dto.UserCalendarTopicRes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCalendarService {

    private final UserCalendarFacade userCalendarFacade;
    private final TopicFacade topicFacade;

    public UserCalendarDiarySummaryRes getDiarySummary(final LocalDate date, final Long userId) {
        validateNotFuture(date);

        var raw = userCalendarFacade.findDiaryByDate(userId, date);

        return new UserCalendarDiarySummaryRes(
                raw.diaryId(),
                raw.createdAt(),
                S3UrlResolver.resolve(raw.imageUrl()),
                raw.originalText()
        );

    }

    public static void validateNotFuture(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new FutureDateNotAllowedException(UserCalendarApiErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }
    }

    public UserCalendarTopicRes getTopicByDate(final LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new FutureDateNotAllowedException(UserCalendarApiErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }
        return topicFacade.findTopicByDate(date);
    }

    public UserCalendarMonthlyRes getWrittenDatesOfMonth(final Long userId, final int year, final int month) {
        List<LocalDate> writtenDates = userCalendarFacade.findWrittenDatesByMonth(userId, year, month);
        return UserCalendarMonthlyRes.from(writtenDates);
    }

}
