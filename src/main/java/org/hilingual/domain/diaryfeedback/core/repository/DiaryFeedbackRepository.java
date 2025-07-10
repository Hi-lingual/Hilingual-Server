package org.hilingual.domain.diaryfeedback.core.repository;


import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryFeedbackRepository extends JpaRepository<DiaryFeedback, Long> {

    List<DiaryFeedback> findByDiaryId(Long diaryId);

}