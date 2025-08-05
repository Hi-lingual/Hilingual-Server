package org.sopt.diaryfeedback.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.sopt.diaryfeedback.repository.DiaryFeedbackRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiaryFeedbackRetriever {

    private final DiaryFeedbackRepository diaryFeedBackRepository;

    public List<DiaryFeedback> findByDiaryId(final long diaryId){
        return diaryFeedBackRepository.findByDiaryId(diaryId);
    }

}