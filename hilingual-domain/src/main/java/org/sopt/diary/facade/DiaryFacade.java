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
    private final DiaryUpdater diaryUpdater;

    /*
     * Retriever
     */
    // 특정 일기가 해당 userId 소유인지 검증
    public void validateDiaryOwnership(final long userId, final long diaryId) {
        Diary diary = diaryRetriever.findById(diaryId);
        if (!diary.getUser().getId().equals(userId)) {
            throw new DiaryForbiddenException(DiaryCoreErrorCode.DIARY_FORBIDDEN);
        }
    }

    // 읽기 권한 검증 (내 일기 or 공개 일기)
    public void validateReadable(final long userId, final long diaryId) {
        Diary diary = diaryRetriever.findById(diaryId);
        boolean mine = diary.getUser().getId().equals(userId);
        boolean publicDiary = Boolean.TRUE.equals(diary.getIsPublic());

        if (!mine && !publicDiary) {
            throw new DiaryForbiddenException(DiaryCoreErrorCode.DIARY_FORBIDDEN);
        }
    }

    public List<Diary> getPublicDiaries(final long userId) {
        return diaryRetriever.findByUserIdAndIsPublicTrue(userId);
    }

    public Diary getDiaryWithDetails(final long diaryId) {
        return diaryRetriever.findDiaryWithDetails(diaryId);
    }

    // 같은 날짜에 이미 일기를 작성했는지 검증
    public void validateNotExists(User user, LocalDate writtenDate) {
        diaryRetriever.validateDiaryNotExists(user, writtenDate);
    }

    // 단건 조회
    public Diary getDiaryById(long diaryId) {
        return diaryRetriever.findById(diaryId);
    }

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
    @Transactional
    public void deleteDiary(final long userId, final long diaryId) {
        diaryRemover.deleteDiary(userId, diaryId);
    }

    /*
     * Updater
     */
    @Transactional
    public void publish(Long userId, Long diaryId) {
        Diary diary = diaryRetriever.findOwnedByIdOrThrow(userId, diaryId);
        diaryUpdater.publish(diary);
    }

    @Transactional
    public void unpublish(Long userId, Long diaryId) {
        Diary diary = diaryRetriever.findOwnedByIdOrThrow(userId, diaryId);
        diaryUpdater.unpublish(diary);
    }

    @Transactional
    public void increaseLikeCount(Long diaryId) {
        Diary diary = diaryRetriever.findById(diaryId);
        diaryUpdater.increaseLike(diary);
    }

    @Transactional
    public void decreaseLikeCount(Long diaryId) {
        Diary diary = diaryRetriever.findById(diaryId);
        diaryUpdater.decreaseLike(diary);
    }

}