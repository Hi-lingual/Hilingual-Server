package org.hilingual.domain.usercalendar.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarDiarySummaryResponse;
import org.hilingual.domain.usercalendar.api.exception.FutureDateNotAllowedException;
import org.hilingual.domain.usercalendar.api.exception.UserCalendarApiErrorCode;
import org.hilingual.domain.usercalendar.core.facade.UserCalendarRetriever;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCalendarService {

    private final UserCalendarRetriever userCalendarRetriever;

    public UserCalendarDiarySummaryResponse getDiarySummary(final LocalDate date, final Long userId) {
        if (date.isAfter(LocalDate.now())) {
            throw new FutureDateNotAllowedException(UserCalendarApiErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }
        return userCalendarRetriever.findDiaryByDate(userId, date);
    }
}
