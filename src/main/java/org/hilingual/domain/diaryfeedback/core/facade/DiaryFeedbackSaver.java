package org.hilingual.domain.diaryfeedback.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.hilingual.domain.diaryfeedback.core.repository.DiaryFeedbackRepository;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryFeedbackSaver {

    private final DiaryFeedbackRepository diaryFeedbackRepository;

    public DiaryFeedback save(DiaryFeedback diaryFeedback){
        return diaryFeedbackRepository.save(diaryFeedback);
    }
}