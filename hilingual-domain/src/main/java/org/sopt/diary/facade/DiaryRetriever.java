package org.sopt.diary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.exception.DiaryAlreadyWrittenException;
import org.sopt.diary.exception.DiaryCoreErrorCode;
import org.sopt.diary.exception.DiaryForbiddenException;
import org.sopt.diary.exception.DiaryNotFoundException;
import org.sopt.diary.repository.DiaryRepository;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiaryRetriever {

    private final DiaryRepository diaryRepository;

    public void validateDiaryNotExists(User user, LocalDate writtenDate) {
        if (diaryRepository.existsByUserAndWrittenDate(user, writtenDate)) {
            throw new DiaryAlreadyWrittenException(DiaryCoreErrorCode.ALREADY_WRITTEN_DIARY);
        }
    }

    public Diary findById(final long diaryId) {
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(DiaryCoreErrorCode.DIARY_NOT_FOUND));
    }

    public List<Diary> findByUserIdAndIsPublicTrue(final Long userId) {
        return diaryRepository.findByUserIdAndIsPublicTrueOrderByCreatedAtDesc(userId);
    }

    public Diary findOwnedByIdOrThrow(Long userId, Long diaryId) {
        return diaryRepository.findByIdAndUserId(diaryId, userId)
                .orElseThrow(() -> new DiaryForbiddenException(DiaryCoreErrorCode.DIARY_FORBIDDEN));
    }

    public Diary findDiaryWithDetails(final long diaryId) {
        return diaryRepository.findDiaryWithDetailsById(diaryId)
                .orElseThrow(() -> new DiaryNotFoundException(DiaryCoreErrorCode.DIARY_NOT_FOUND));
    }
}
