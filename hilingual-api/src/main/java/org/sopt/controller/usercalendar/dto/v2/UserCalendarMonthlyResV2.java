package org.sopt.controller.usercalendar.dto.v2;

import org.sopt.diary.domain.type.DiaryStatus;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public record UserCalendarMonthlyResV2(
        List<DateDto> dateList
) {
    public static UserCalendarMonthlyResV2 fromMap(Map<LocalDate, DiaryStatus> statusMap) {
        List<DateDto> dtoList = statusMap.entrySet().stream()
                .map(entry -> new DateDto(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(DateDto::date)) // 날짜순 정렬
                .toList();

        return new UserCalendarMonthlyResV2(dtoList);
    }

    public record DateDto(LocalDate date, DiaryStatus status) {}
}