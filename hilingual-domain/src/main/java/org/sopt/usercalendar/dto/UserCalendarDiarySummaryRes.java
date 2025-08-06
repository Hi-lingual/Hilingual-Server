package org.sopt.usercalendar.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record UserCalendarDiarySummaryRes(
        Long diaryId,
        String createdAt,
        String imageUrl,
        String originalText
) {
    public static UserCalendarDiarySummaryRes of(
            Long diaryId,
            LocalDateTime createdAt,
            String imageUrl,
            String originalText
    ) {
        String formattedTime = createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        return new UserCalendarDiarySummaryRes(diaryId, formattedTime, imageUrl, originalText);
    }
}
