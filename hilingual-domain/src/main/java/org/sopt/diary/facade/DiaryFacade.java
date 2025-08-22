package org.sopt.diary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.exception.DiaryCoreErrorCode;
import org.sopt.diary.exception.DiaryForbiddenException;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DiaryFacade {

    private final DiaryRetriever diaryRetriever;
    private final DiarySaver diarySaver;
    private final DiaryRemover diaryRemover;

    /*
     * Retriever
     */
    public Diary getDiaryById(long diaryId) {
        return diaryRetriever.findById(diaryId);
    }

    public List<Diary> getPublicDiaries(final long userId) { return diaryRetriever.findByUserIdAndIsPublicTrue(userId); }

    public Diary getDiaryWithDetails(final long diaryId) { return diaryRetriever.findDiaryWithDetails(diaryId); }

    /*
     * Saver
     */
    @Transactional
    public Diary saveDiary(User user, String originalText, String rewriteText, String imageUrl, LocalDate writtenDate) {
        return diarySaver.save(user, originalText, rewriteText, imageUrl, writtenDate);
    }

    /*
     * Remover
     */
    public void deleteDiary(final long userId, final long diaryId) {
        diaryRemover.deleteDiary(userId,diaryId);
    }

    public void validateDiaryOwnership(final long userId, final long diaryId) {
        Diary diary = diaryRetriever.findById(diaryId);
        if (!diary.getUser().getId().equals(userId)) {
            throw new DiaryForbiddenException(DiaryCoreErrorCode.DIARY_FORBIDDEN);
        }
    }

    public void validateNotExists(User user, LocalDate writtenDate) {
        diaryRetriever.validateDiaryNotExists(user, writtenDate);
    }

}
