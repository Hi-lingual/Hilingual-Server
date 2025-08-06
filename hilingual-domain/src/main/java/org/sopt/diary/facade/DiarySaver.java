package org.sopt.diary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.repository.DiaryRepository;
import org.sopt.user.domain.User;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.sopt.userprofile.facade.UserProfileUpdater;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DiarySaver {

    private final DiaryRepository diaryRepository;
    private final UserProfileFacade userProfileFacade;
    private final UserCalendarFacade userCalendarFacade;

    @Transactional
    public Diary save(
            final User user,
            final String originalText,
            final String rewriteText,
            final String imageUrl,
            final LocalDate writtenDate
    ) {
        Diary saved = diaryRepository.save(
                Diary.create(user, originalText, rewriteText, imageUrl, writtenDate)
        );

        userCalendarFacade.markWrittenDate(user, writtenDate);
        userProfileFacade.incrementTotalDiaries(user.getId());
        userProfileFacade.updateStreakOnWrite(user.getId(), writtenDate);

        return saved;
    }

}
