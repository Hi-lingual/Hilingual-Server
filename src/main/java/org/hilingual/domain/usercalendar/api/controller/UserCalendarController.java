package org.hilingual.domain.usercalendar.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarDiarySummaryResponse;
import org.hilingual.domain.usercalendar.api.service.UserCalendarService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/calendar")
@RequiredArgsConstructor
public class UserCalendarController {

    private final UserCalendarService userCalendarService;

    @GetMapping("/{date}")
    public ResponseEntity<UserCalendarDiarySummaryResponse> getDiarySummaryByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate date
    ) {
        final Long userId = 1L; // TODO: 로그인 연동 후 대체
        return ResponseEntity.ok(userCalendarService.getDiarySummary(date, userId));
    }
}
