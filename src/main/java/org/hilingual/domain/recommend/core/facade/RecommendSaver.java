package org.hilingual.domain.recommend.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.hilingual.domain.recommend.core.repository.RecommendRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendSaver {

    private final RecommendRepository recommendRepository;

    public void save(Recommend recommend) {
        recommendRepository.save(recommend);
    }
}