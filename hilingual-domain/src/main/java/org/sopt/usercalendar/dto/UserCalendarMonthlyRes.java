package org.sopt.usercalendar.dto;

import java.time.LocalDate;
import java.util.List;

public record UserCalendarMonthlyRes(
        List<DateDto> dateList
) {
    public static UserCalendarMonthlyRes from(List<LocalDate> dates) {
        return new UserCalendarMonthlyRes(
                dates.stream().map(DateDto::new).toList()
        );
    }

    public record DateDto(LocalDate date) {}
}
