package org.sopt.diary.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.repository.DiaryRepository;
import org.sopt.diaryfeedback.repository.DiaryFeedbackRepository;
import org.sopt.recommend.facade.RecommendFacade;
import org.sopt.recommend.repository.RecommendRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiaryRemover {

    private final DiaryRepository diaryRepository;
    private final RecommendRepository recommendRepository;
    private final DiaryFeedbackRepository diaryFeedbackRepository;

    public void deleteAllByUserId(final Long userId) {
        recommendRepository.deleteAllByUserId(userId);
        diaryFeedbackRepository.deleteAllByUserId(userId);
        diaryRepository.deleteAllByUserId(userId);
    }

}