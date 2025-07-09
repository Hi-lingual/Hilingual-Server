package org.hilingual.domain.diaryfeedback.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diaryfeedback.api.dto.DiaryFeedbackList;
import org.hilingual.domain.diaryfeedback.core.domain.DiaryFeedback;
import org.hilingual.domain.diaryfeedback.core.facade.DiaryFeedbackRetriever;
import org.hilingual.domain.diaryfeedback.core.facade.DiaryFeedbackSaver;
import org.hilingual.domain.diaryfeedback.core.facade.DiaryValidator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryFeedbackService {

    private final DiaryFeedbackSaver diaryFeedbackSaver;
    private final DiaryFeedbackRetriever diaryFeedbackRetriever;
    private final DiaryValidator diaryValidator;

    @Transactional
    public void saveFeedback(DiaryFeedback diaryFeedback) {
        diaryFeedbackSaver.save(diaryFeedback);
    }

    public DiaryFeedbackList getFeedbackList(final long userId, final long diaryId){
        diaryValidator.validateDiaryOwnership(userId, diaryId);

        List<DiaryFeedbackList.DiaryFeedbackDto> feedbacks =
                diaryFeedbackRetriever.findByDiaryId(diaryId).stream()
                .map(f -> new DiaryFeedbackList.DiaryFeedbackDto(
                        f.getOriginPhrase(),
                        f.getRewritePhrase(),
                        f.getExplanation()
                ))
                .collect(Collectors.toList());

        return new DiaryFeedbackList(feedbacks);
    }

}