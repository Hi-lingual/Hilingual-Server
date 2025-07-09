package org.hilingual.domain.recommend.api.service;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.recommend.core.facade.RecommendSaver;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RecommendService {

    private final RecommendSaver recommendSaver;

    @Transactional
    public void saveRecommend(Recommend recommend) {
        recommendSaver.save(recommend);
    }
}