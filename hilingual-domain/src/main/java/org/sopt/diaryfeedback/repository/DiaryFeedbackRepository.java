package org.sopt.diaryfeedback.repository;

import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryFeedbackRepository extends JpaRepository<DiaryFeedback, Long> {

    List<DiaryFeedback> findByDiaryId(Long diaryId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from DiaryFeedback df where df.diary.user.id = :userId")
    void deleteAllByUserId(Long userId);
}