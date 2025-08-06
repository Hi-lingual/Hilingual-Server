package org.sopt.diaryfeedback.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiaryFeedbackFacade {

    private final DiaryFeedbackRetriever diaryFeedbackRetriever;
    private final DiaryFeedbackSaver diaryFeedbackSaver;


    public DiaryFeedback save(DiaryFeedback diaryFeedback) {
        return diaryFeedbackSaver.save(diaryFeedback);
    }

    public List<DiaryFeedback> findByDiaryId(final long diaryId) {
        return diaryFeedbackRetriever.findByDiaryId(diaryId);
    }

}
