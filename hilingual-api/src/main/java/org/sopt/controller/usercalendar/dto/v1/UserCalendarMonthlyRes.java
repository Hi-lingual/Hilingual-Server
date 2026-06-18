package org.sopt.controller.usercalendar.dto.v1;

import java.time.LocalDate;
import java.util.List;

public record UserCalendarMonthlyRes(
        List<DateInfo> dateList
) {
    public record DateInfo(LocalDate date) {}

    public static UserCalendarMonthlyRes from(List<LocalDate> dates) {
        List<DateInfo> dateInfoList = dates.stream()
                .map(DateInfo::new)
                .toList();

        return new UserCalendarMonthlyRes(dateInfoList);
    }
}