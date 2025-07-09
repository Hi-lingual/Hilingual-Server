package org.hilingual.domain.recommend.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.hilingual.domain.recommend.core.repository.RecommendRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendRetriever {

    private final RecommendRepository recommendRepository;

    public List<Recommend> findByDiaryId(final long diaryId){
        return recommendRepository.findByDiaryId(diaryId);
    }

}