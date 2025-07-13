package org.hilingual.domain.diary.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diary.core.exception.DiaryCoreErrorCode;
import org.hilingual.domain.diary.core.exception.DiaryNotFoundException;
import org.hilingual.domain.diary.core.repository.DiaryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DiaryRetriever {

    private final DiaryRepository diaryRepository;

    public Diary findById(final long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(DiaryCoreErrorCode.DIARY_NOT_FOUND));
    }

    public List<LocalDateTime> findDiaryCreatedAts(final Long userId) {
        return diaryRepository.findCreatedAtsByUserId(userId);
    }

    public Optional<LocalDateTime> findLatestDiaryCreatedAt(final Long userId) {
        return diaryRepository.findLatestCreatedAtByUserId(userId);
    }


}
