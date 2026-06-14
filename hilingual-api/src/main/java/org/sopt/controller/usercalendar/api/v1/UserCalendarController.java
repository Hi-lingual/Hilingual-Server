package org.sopt.controller.usercalendar.api.v1;

import lombok.RequiredArgsConstructor;
import org.sopt.annotation.UserTimezone;
import org.sopt.diary.domain.type.DiaryStatus;
import org.sopt.jwt.annotation.UserId;
import org.sopt.usercalendar.dto.UserCalendarDiarySummaryRes;
import org.sopt.controller.usercalendar.dto.v1.UserCalendarMonthlyRes;
import org.sopt.usercalendar.dto.UserCalendarTopicRes;
import org.sopt.controller.usercalendar.exception.InvalidMonthException;
import org.sopt.controller.usercalendar.exception.UserCalendarApiErrorCode;
import org.sopt.controller.usercalendar.exception.UserCalendarInvalidDateFormatException;
import org.sopt.controller.usercalendar.service.UserCalendarService;
import org.sopt.web.UserZone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/home/calendar")
@RequiredArgsConstructor
public class UserCalendarController {

    private final UserCalendarService userCalendarService;

    @GetMapping("/{date}")
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

    @GetMapping("/{date}/topic")
    public ResponseEntity<UserCalendarTopicRes> getTopicByDate(
            @UserId Long userId,
            @UserTimezone final UserZone userZone,
            @PathVariable final String date
    ) {
        final LocalDate parsedDate;

        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new UserCalendarInvalidDateFormatException(UserCalendarApiErrorCode.INVALID_DATE_FORMAT);
        }

        return ResponseEntity.ok(userCalendarService.getTopicByDate(userId, parsedDate, userZone.zoneId()));
    }

    @GetMapping("/month")
    public ResponseEntity<UserCalendarMonthlyRes> getMonthlyCalendar(
            @UserId Long userId,
            @RequestParam final int year,
            @RequestParam final int month
    ) {
        if (month < 1 || month > 12) {
            throw new InvalidMonthException(UserCalendarApiErrorCode.INVALID_MONTH);
        }

        Map<LocalDate, DiaryStatus> statusMap = userCalendarService.getMonthlyCalendarStatus(userId, year, month);
        List<LocalDate> writtenDates = statusMap.entrySet().stream()
                .filter(entry -> entry.getValue() == DiaryStatus.WRITTEN || entry.getValue() == DiaryStatus.RECOVERED)
                .map(Map.Entry::getKey)
                .sorted()
                .toList();

        return ResponseEntity.ok(UserCalendarMonthlyRes.from(writtenDates));
    }

}
