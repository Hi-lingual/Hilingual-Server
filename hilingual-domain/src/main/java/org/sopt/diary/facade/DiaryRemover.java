package org.sopt.diary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.exception.DiaryCoreErrorCode;
import org.sopt.diary.exception.DiaryForbiddenException;
import org.sopt.diary.exception.DiaryNotFoundException;
import org.sopt.diary.repository.DiaryRepository;
import org.sopt.userprofile.facade.UserProfileFacade;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DiaryRemover {

    private final DiaryRepository diaryRepository;
    private final UserProfileFacade userProfileFacade;

    @Transactional
    public void deleteDiary(final Long userId, final Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(DiaryCoreErrorCode.DIARY_NOT_FOUND));

        if (!diary.getUser().getId().equals(userId)) {
            throw new DiaryForbiddenException(DiaryCoreErrorCode.DIARY_FORBIDDEN);
        }
        diaryRepository.delete(diary);
        userProfileFacade.decrementTotalDiariesAndRecalculateStreak(userId);
    }

    public void deleteAllByUserId(final Long userId) {
        diaryRepository.deleteAllByUserId(userId);
    }

}