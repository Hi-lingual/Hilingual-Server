package org.hilingual.domain.usercalendar.api.dto.res;

import java.time.LocalDate;
import java.util.List;

public record UserCalendarMonthlyResponse(
        List<DateDto> dateList
) {
    public static UserCalendarMonthlyResponse from(List<LocalDate> dates) {
        return new UserCalendarMonthlyResponse(
                dates.stream().map(DateDto::new).toList()
        );
    }

    public record DateDto(LocalDate date) {}
}
