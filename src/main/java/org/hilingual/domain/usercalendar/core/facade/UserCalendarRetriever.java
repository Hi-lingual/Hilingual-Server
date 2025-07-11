package org.hilingual.domain.usercalendar.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diary.core.repository.DiaryRepository;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarDiarySummaryResponse;
import org.hilingual.domain.usercalendar.api.exception.UserCalendarDiaryNotFoundException;
import org.hilingual.domain.usercalendar.core.exception.UserCalendarCoreErrorCode;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UserCalendarRetriever {

    private final DiaryRepository diaryRepository;

    public UserCalendarDiarySummaryResponse findDiaryByDate(final Long userId, final LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay(); // 다음날 00:00

        return diaryRepository.findByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay)
                .stream()
                .findFirst()
                .map(d ->
                        UserCalendarDiarySummaryResponse.of(
                                d.getId(),
                                d.getCreatedAt(),
                                d.getImageUrl(),
                                d.getOriginalText()
                        )
                )
                .orElseThrow(() ->
                        new UserCalendarDiaryNotFoundException(UserCalendarCoreErrorCode.DIARY_NOT_FOUND)
                );
    }
}
