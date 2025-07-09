package org.hilingual.domain.diaryfeedback.core.repository;

import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DiaryFeedbackRepository extends JpaRepository<DiaryFeedback, Long> {

    List<DiaryFeedback> findByDiaryId(Long diaryId);

}