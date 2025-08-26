package org.sopt.recommend.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recommend.domain.Recommend;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class RecommendFacade {

    private final RecommendRetriever recommendRetriever;
    private final RecommendSaver recommendSaver;

    public void save(Recommend recommend) {
        recommendSaver.save(recommend);
    }

    public List<Recommend> findByDiaryId(final long diaryId) {
        return recommendRetriever.findByDiaryId(diaryId);
    }

    public Recommend findById(final long phraseId) {
        return recommendRetriever.findById(phraseId);
    }

}
