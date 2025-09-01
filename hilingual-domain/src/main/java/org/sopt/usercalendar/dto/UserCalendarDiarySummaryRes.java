package org.sopt.usercalendar.dto;

import org.sopt.diary.domain.Diary;

public record UserCalendarDiarySummaryRes(
        Long diaryId,
        String imageUrl,
        String originalText,
        Boolean isPublished
) {
    public static UserCalendarDiarySummaryRes of(Diary diary, String diaryImgUrl) {
        return new UserCalendarDiarySummaryRes(
                diary.getId(),
                diaryImgUrl,
                diary.getOriginalText(),
                diary.getIsPublic()
        );
    }
}
