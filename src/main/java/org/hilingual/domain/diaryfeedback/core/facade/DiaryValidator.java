package org.hilingual.domain.diaryfeedback.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diary.core.exception.DiaryCoreErrorCode;
import org.hilingual.domain.diary.core.exception.DiaryForbiddenException;
import org.hilingual.domain.diary.core.facade.DiaryRetriever;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryValidator {

    private final DiaryRetriever diaryRetriever;

    public void validateDiaryOwnership(final long userId, final long diaryId) {
        Diary diary = diaryRetriever.findById(diaryId);
        if (!diary.getUser().getId().equals(userId)) {
            throw new DiaryForbiddenException(DiaryCoreErrorCode.DIARY_FORBIDDEN);
        }
    }
}