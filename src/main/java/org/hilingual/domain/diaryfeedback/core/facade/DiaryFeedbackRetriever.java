package org.hilingual.domain.diaryfeedback.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.hilingual.domain.diaryfeedback.core.repository.DiaryFeedBackRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DiaryFeedbackRetriever {

    private final DiaryFeedBackRepository diaryFeedBackRepository;

    public List<DiaryFeedback> findByDiaryId(final long diaryId){
        return diaryFeedBackRepository.findByDiaryId(diaryId);
    }

}