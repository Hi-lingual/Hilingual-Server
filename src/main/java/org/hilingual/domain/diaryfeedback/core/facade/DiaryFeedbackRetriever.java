package org.hilingual.domain.diaryfeedback.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.domain.Diary;
rg.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.hilingual.domain.diaryfeedback.core.repository.DiaryFeedbackRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiaryFeedbackRetriever {

    private final DiaryFeedbackRepository diaryFeedBackRepository;

    public List<DiaryFeedback> findByDiaryId(final long diaryId){
        return diaryFeedbackRepository.findByDiaryId(diaryId);
    }

}