package org.sopt.diary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.domain.DiaryTableConstants;
import org.sopt.diary.exception.DiaryAlreadyWrittenException;
import org.sopt.diary.exception.DiaryCoreErrorCode;
import org.sopt.diary.repository.DiaryRepository;
import org.sopt.user.domain.User;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            Diary saved = diaryRepository.save(
                    Diary.create(user, originalText, rewriteText, imageUrl, writtenDate)
            );

            userCalendarFacade.markWrittenDate(user, writtenDate);
            userProfileFacade.incrementTotalDiariesAndRecalculateStreak(user.getId(), writtenDate);

            return saved;
        } catch (DataIntegrityViolationException ex) {
            if (isDuplicateDiaryKey(ex)) {
                throw new DiaryAlreadyWrittenException(DiaryCoreErrorCode.ALREADY_WRITTEN_DIARY);
            }
            throw ex;
        }
    }

    private boolean isDuplicateDiaryKey(DataIntegrityViolationException ex) {
        Throwable cause = ex.getMostSpecificCause();
        String message = cause.getMessage();
        if (message == null) {
            return false;
        }
        return message.contains(DiaryTableConstants.UK_DIARY_USER_WRITTEN_DATE);
    }
}
