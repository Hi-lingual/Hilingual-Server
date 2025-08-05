package org.sopt.diaryfeedback.repository;

import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryFeedbackRepository extends JpaRepository<DiaryFeedback, Long> {

    List<DiaryFeedback> findByDiaryId(Long diaryId);

}