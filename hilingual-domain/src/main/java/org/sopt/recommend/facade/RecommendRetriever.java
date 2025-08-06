package org.sopt.recommend.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recommend.domain.Recommend;
import org.sopt.recommend.exception.RecommendCoreErrorCode;
import org.sopt.recommend.exception.RecommendNotFoundException;
import org.sopt.recommend.repository.RecommendRepository;
import org.sopt.voca.exception.VocaCoreErrorCode;
import org.sopt.voca.exception.VocaNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendRetriever {

    private final RecommendRepository recommendRepository;

    public List<Recommend> findByDiaryId(final long diaryId){
        return recommendRepository.findByDiaryId(diaryId);
    }

    public Recommend findById(final long phraseId){
        return recommendRepository.findById(phraseId)
                .orElseThrow(()-> new RecommendNotFoundException(RecommendCoreErrorCode.RECOMMEND_NOT_FOUND));
    }

    public Recommend findByUserIdAndPhraseId(final Long userId, final Long phraseId) {
        return recommendRepository.findPhraseByIdAndUserId(phraseId, userId)
                .orElseThrow(() -> new VocaNotFoundException(VocaCoreErrorCode.VOCA_NOT_FOUND));
    }

}