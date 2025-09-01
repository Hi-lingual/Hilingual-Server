package org.sopt.controller.usercalendar.api;

import lombok.RequiredArgsConstructor;
import org.sopt.jwt.annotation.UserId;
import org.sopt.usercalendar.dto.UserCalendarDiarySummaryRes;
import org.sopt.usercalendar.dto.UserCalendarMonthlyRes;
import org.sopt.usercalendar.dto.UserCalendarTopicRes;
import org.sopt.controller.usercalendar.exception.InvalidMonthException;
import org.sopt.controller.usercalendar.exception.UserCalendarApiErrorCode;
import org.sopt.controller.usercalendar.exception.UserCalendarInvalidDateFormatException;
import org.sopt.controller.usercalendar.service.UserCalendarService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class UserCalendarController {

    private final UserCalendarService userCalendarService;

    @GetMapping("/home/calendar/{date}")
    public ResponseEntity<UserCalendarDiarySummaryRes> getDiarySummaryByDate(
            @UserId Long userId,
            @PathVariable final String date
    ) {
        final LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new UserCalendarInvalidDateFormatException(UserCalendarApiErrorCode.INVALID_DATE_FORMAT);
        }

        return ResponseEntity.ok(userCalendarService.getDiarySummary(parsedDate, userId));
    }

    @GetMapping("/calendar/{date}/topic")
    public ResponseEntity<UserCalendarTopicRes> getTopicByDate(@PathVariable final String date) {
        final LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new UserCalendarInvalidDateFormatException(UserCalendarApiErrorCode.INVALID_DATE_FORMAT);
        }

        return ResponseEntity.ok(userCalendarService.getTopicByDate(parsedDate));
    }

    @GetMapping("/calendar/month")
    public ResponseEntity<UserCalendarMonthlyRes> getMonthlyCalendar(
            @UserId Long userId,
            @RequestParam final int year,
            @RequestParam final int month
    ) {

        if (month < 1 || month > 12) {
            throw new InvalidMonthException(UserCalendarApiErrorCode.INVALID_MONTH);
        }
        return ResponseEntity.ok(userCalendarService.getWrittenDatesOfMonth(userId, year, month));
    }

}
