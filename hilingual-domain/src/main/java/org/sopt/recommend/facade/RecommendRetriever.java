package org.sopt.recommend.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recommend.domain.Recommend;
import org.sopt.recommend.dto.RecommendWithBookmarkDto;
import org.sopt.recommend.exception.RecommendCoreErrorCode;
import org.sopt.recommend.exception.RecommendNotFoundException;
import org.sopt.recommend.repository.RecommendRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendRetriever {

    private final RecommendRepository recommendRepository;

    public Recommend findById(final long phraseId){
        return recommendRepository.findById(phraseId)
                .orElseThrow(()-> new RecommendNotFoundException(RecommendCoreErrorCode.RECOMMEND_NOT_FOUND));
    }

    public List<RecommendWithBookmarkDto> findWithBookmarkFlag(final long userId, final long diaryId) {
        return recommendRepository.findWithBookmarkFlag(userId, diaryId);
    }

    public Recommend findByIdWithDiary(final long phraseId) {
        return recommendRepository.findByIdWithDiary(phraseId)
                .orElseThrow(() ->
                        new RecommendNotFoundException(RecommendCoreErrorCode.RECOMMEND_NOT_FOUND));
    }

}