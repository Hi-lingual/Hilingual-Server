package org.hilingual.domain.recommend.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.hilingual.domain.recommend.core.exception.RecommendCoreErrorCode;
import org.hilingual.domain.recommend.core.exception.RecommendNotFoundException;
import org.hilingual.domain.recommend.core.repository.RecommendRepository;
import org.hilingual.domain.voca.core.exception.VocaCoreErrorCode;
import org.hilingual.domain.voca.core.exception.VocaNotFoundException;
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