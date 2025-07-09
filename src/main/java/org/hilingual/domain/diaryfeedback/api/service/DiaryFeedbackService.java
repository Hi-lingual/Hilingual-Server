package org.hilingual.domain.diaryfeedback.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.hilingual.domain.diaryfeedback.core.facade.DiaryFeedbackSaver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DiaryFeedbackService {

    private final DiaryFeedbackSaver diaryFeedbackSaver;

    @Transactional
    public void saveFeedback(DiaryFeedback diaryFeedback) {
        diaryFeedbackSaver.save(diaryFeedback);
    }
}