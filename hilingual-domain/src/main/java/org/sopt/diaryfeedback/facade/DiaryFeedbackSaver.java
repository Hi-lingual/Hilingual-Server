package org.sopt.diaryfeedback.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.sopt.diaryfeedback.repository.DiaryFeedbackRepository;

import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryFeedbackSaver {

    private final DiaryFeedbackRepository diaryFeedbackRepository;

    public DiaryFeedback save(DiaryFeedback diaryFeedback){
        return diaryFeedbackRepository.save(diaryFeedback);
    }
}