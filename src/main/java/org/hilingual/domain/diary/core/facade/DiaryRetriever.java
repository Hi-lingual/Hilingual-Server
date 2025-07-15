package org.hilingual.domain.diary.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diary.core.exception.AlreadyWrittenDiaryException;
import org.hilingual.domain.diary.core.exception.DiaryCoreErrorCode;
import org.hilingual.domain.diary.core.exception.DiaryNotFoundException;
import org.hilingual.domain.diary.core.repository.DiaryRepository;
import org.hilingual.domain.user.core.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DiaryRetriever {

    private final DiaryRepository diaryRepository;

    public void validateDiaryNotExists(User user, LocalDate writtenDate) {
        if (diaryRepository.existsByUserAndWrittenDate(user, writtenDate)) {
            throw new AlreadyWrittenDiaryException(DiaryCoreErrorCode.ALREADY_WRITTEN_DIARY);
        }
    }

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
