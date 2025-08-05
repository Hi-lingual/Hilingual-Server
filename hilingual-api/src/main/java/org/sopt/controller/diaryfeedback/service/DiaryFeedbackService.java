package org.sopt.controller.diaryfeedback.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.diaryfeedback.dto.DiaryFeedbackListRes;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.diaryfeedback.domain.DiaryFeedback;
import org.sopt.diaryfeedback.facade.DiaryFeedbackFacade;
import org.sopt.diaryfeedback.facade.DiaryFeedbackRetriever;
import org.sopt.diaryfeedback.facade.DiaryFeedbackSaver;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DiaryFeedbackService {

    private final DiaryFeedbackFacade diaryFeedbackFacade;
    private final DiaryFacade diaryFacade;

    @Transactional
    public void saveFeedback(DiaryFeedback diaryFeedback) {
        diaryFeedbackFacade.save(diaryFeedback);
    }

    public DiaryFeedbackListRes getFeedbackList(final long userId, final long diaryId){
        diaryFacade.validateDiaryOwnership(userId, diaryId);

        List<DiaryFeedbackListRes.DiaryFeedbackDto> feedbacks =
                diaryFeedbackFacade.findByDiaryId(diaryId).stream()
                .map(f -> new DiaryFeedbackListRes.DiaryFeedbackDto(
                        f.getOriginPhrase(),
                        f.getRewritePhrase(),
                        f.getExplanation()
                ))
                .collect(Collectors.toList());

        return new DiaryFeedbackListRes(feedbacks);
    }

}