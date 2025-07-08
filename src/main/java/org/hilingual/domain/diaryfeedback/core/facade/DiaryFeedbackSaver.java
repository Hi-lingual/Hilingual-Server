package org.hilingual.domain.diaryfeedback.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.hilingual.domain.diaryfeedback.core.repository.DiaryFeedBackRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryFeedbackSaver {

    private final DiaryFeedBackRepository diaryFeedbackRepository;

    public void save(DiaryFeedback diaryFeedback) {
        diaryFeedbackRepository.save(diaryFeedback);
    }
}