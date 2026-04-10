package org.sopt.controller.usercalendar.service;

import lombok.RequiredArgsConstructor;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.controller.usercalendar.exception.FutureDateNotAllowedException;
import org.sopt.controller.usercalendar.exception.UserCalendarApiErrorCode;
import org.sopt.diary.domain.Diary;
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
    private final S3Service s3Service;

    public UserCalendarDiarySummaryRes getDiarySummary(final LocalDate date, final Long userId) {
        validateNotFuture(date);

        final Diary diary = userCalendarFacade.findDiaryByDate(userId, date);
        final String diaryImgUrl = s3Service.toPublicUrl(diary.getImageUrl());
        return UserCalendarDiarySummaryRes.of(diary, diaryImgUrl);

    }

    public static void validateNotFuture(LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new FutureDateNotAllowedException(UserCalendarApiErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }
    }

    public UserCalendarTopicRes getTopicByDate(final long userId, final LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new FutureDateNotAllowedException(UserCalendarApiErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }
        return topicFacade.findTopicByDate(userId, date);
    }

    public UserCalendarMonthlyRes getWrittenDatesOfMonth(final Long userId, final int year, final int month) {
        List<LocalDate> writtenDates = userCalendarFacade.findWrittenDatesByMonth(userId, year, month);
        return UserCalendarMonthlyRes.from(writtenDates);
    }

}
