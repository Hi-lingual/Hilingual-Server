package org.hilingual.domain.diary.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diary.core.repository.DiaryRepository;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.usercalendar.api.service.UserCalendarService;
import org.hilingual.domain.userprofile.core.facade.UserProfileUpdater;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DiarySaver {

    private final DiaryRepository diaryRepository;
    private final UserProfileUpdater userProfileUpdater;
    private final UserCalendarService userCalendarService;


    @Transactional
    public Diary save(
            final User user,
            final String originalText,
            final String rewriteText,
            final String imageUrl,
            final LocalDate writtenDate
    ) {
        // 1) 일기 저장
        Diary diary = Diary.create(user, originalText, rewriteText, imageUrl, writtenDate);
        Diary saved = diaryRepository.save(diary);

        // 2) calendar에 기록 → streak 로직보다 **반드시 먼저** 호출
        userCalendarService.markWrittenDate(user, writtenDate);

        // 3) totalDiaries 증가
        userProfileUpdater.incrementTotalDiaries(user.getId());

        // 4) streak 업데이트
        userProfileUpdater.updateStreakOnWrite(user.getId(), writtenDate);

        return saved;
    }
}
