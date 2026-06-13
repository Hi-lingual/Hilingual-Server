package org.sopt.controller.usercalendar.api.v2;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.usercalendar.exception.InvalidMonthException;
import org.sopt.controller.usercalendar.exception.UserCalendarApiErrorCode;
import org.sopt.controller.usercalendar.service.UserCalendarService;
import org.sopt.diary.domain.type.DiaryStatus;
import org.sopt.jwt.annotation.UserId;
import org.sopt.controller.usercalendar.dto.v2.UserCalendarMonthlyResV2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/home/calendar")
@RequiredArgsConstructor
public class UserCalendarControllerV2 {

    private final UserCalendarService userCalendarService;

    @GetMapping("/month")
    public ResponseEntity<UserCalendarMonthlyResV2> getMonthlyCalendar(
            @UserId Long userId,
            @RequestParam final int year,
            @RequestParam final int month
    ) {

        if (month < 1 || month > 12) {
            throw new InvalidMonthException(UserCalendarApiErrorCode.INVALID_MONTH);
        }

        Map<LocalDate, DiaryStatus> statusMap = userCalendarService.getMonthlyCalendarStatus(userId, year, month);
        return ResponseEntity.ok(UserCalendarMonthlyResV2.fromMap(statusMap));
    }

}
