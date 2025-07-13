package org.hilingual.domain.usercalendar.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarDiarySummaryResponse;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarTopicResponse;
import org.hilingual.domain.usercalendar.api.exception.UserCalendarApiErrorCode;
import org.hilingual.domain.usercalendar.api.exception.UserCalendarInvalidDateFormatException;
import org.hilingual.domain.usercalendar.api.service.UserCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
public class UserCalendarController {

    private final UserCalendarService userCalendarService;

    @GetMapping("/{date}")
    public ResponseEntity<UserCalendarDiarySummaryResponse> getDiarySummaryByDate(
            @PathVariable final String date
    ) {
        final Long userId = 1L; // TODO: 로그인 연동 후 대체

        final LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new UserCalendarInvalidDateFormatException(UserCalendarApiErrorCode.INVALID_DATE_FORMAT);
        }

        return ResponseEntity.ok(userCalendarService.getDiarySummary(parsedDate, userId));
    }

    @GetMapping("/{date}/topic")
    public ResponseEntity<UserCalendarTopicResponse> getTopicByDate(@PathVariable final String date) {
        final Long userId = 1L; // TODO: 로그인 연동 후 대체
        final LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new UserCalendarInvalidDateFormatException(UserCalendarApiErrorCode.INVALID_DATE_FORMAT);
        }

        return ResponseEntity.ok(userCalendarService.getTopicByDate(parsedDate, userId));
    }
}
