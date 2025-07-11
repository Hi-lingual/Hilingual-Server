package org.hilingual.domain.usercalendar.api.dto.res;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record UserCalendarDiarySummaryResponse(
        Long diaryId,
        String createdAt,
        String imageUrl,
        String originalText
) {
    public static UserCalendarDiarySummaryResponse of(
            Long diaryId,
            LocalDateTime createdAt,
            String imageUrl,
            String originalText
    ) {
        String formattedTime = createdAt.format(DateTimeFormatter.ofPattern("HH:mm"));
        return new UserCalendarDiarySummaryResponse(diaryId, formattedTime, imageUrl, originalText);
    }
}
