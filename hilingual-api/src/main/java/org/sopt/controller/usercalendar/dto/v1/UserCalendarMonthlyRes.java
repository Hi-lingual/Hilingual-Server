package org.sopt.controller.usercalendar.dto.v1;

import java.time.LocalDate;
import java.util.List;

public record UserCalendarMonthlyRes(
        List<LocalDate> dateList
) {
    public static UserCalendarMonthlyRes from(List<LocalDate> dates) {
        return new UserCalendarMonthlyRes(dates);
    }
}