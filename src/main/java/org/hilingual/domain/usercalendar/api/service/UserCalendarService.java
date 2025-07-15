package org.hilingual.domain.usercalendar.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarDiarySummaryResponse;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarMonthlyResponse;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarTopicResponse;
import org.hilingual.domain.usercalendar.api.exception.FutureDateNotAllowedException;
import org.hilingual.domain.usercalendar.api.exception.UserCalendarApiErrorCode;
import org.hilingual.domain.usercalendar.core.facade.UserCalendarRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCalendarService {

    private final UserCalendarRetriever userCalendarRetriever;

    private static final String S3_BASE_URL = "https://hilingual-bucket.s3.ap-northeast-2.amazonaws.com/";

    public void markWrittenDate(final User user, final LocalDate writtenDate){
        userCalendarRetriever.markWrittenDate(user, writtenDate);
    };


    public UserCalendarDiarySummaryResponse getDiarySummary(final LocalDate date, final Long userId) {
        if (date.isAfter(LocalDate.now())) {
            throw new FutureDateNotAllowedException(UserCalendarApiErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }

        UserCalendarDiarySummaryResponse raw =
                userCalendarRetriever.findDiaryByDate(userId, date);

        String absoluteUrl = (raw.imageUrl() != null && !raw.imageUrl().isBlank()
                && !raw.imageUrl().startsWith("http"))
                ? S3_BASE_URL + raw.imageUrl()
                : raw.imageUrl();

        return new UserCalendarDiarySummaryResponse(
                raw.diaryId(),
                raw.createdAt(),
                absoluteUrl,
                raw.originalText()
        );
    }

    public UserCalendarTopicResponse getTopicByDate(final LocalDate date) {
        if (date.isAfter(LocalDate.now())) {
            throw new FutureDateNotAllowedException(UserCalendarApiErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }
        return userCalendarRetriever.findTopicByDate(date);
    }

    public UserCalendarMonthlyResponse getWrittenDatesOfMonth(final Long userId, final int year, final int month) {
        List<LocalDate> writtenDates = userCalendarRetriever.findWrittenDatesByMonth(userId, year, month);
        return UserCalendarMonthlyResponse.from(writtenDates);
    }

}
