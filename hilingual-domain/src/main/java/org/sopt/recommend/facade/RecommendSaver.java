package org.sopt.recommend.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recommend.domain.Recommend;
import org.sopt.recommend.repository.RecommendRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RecommendSaver {

    private final RecommendRepository recommendRepository;

    public void save(Recommend recommend) {
        recommendRepository.save(recommend);
    }
}