package org.hilingual.domain.diaryfeedback.core.repository;

import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryFeedBackRepository extends JpaRepository<DiaryFeedback, Long> {
}
