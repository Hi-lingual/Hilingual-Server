package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VocaFacade {

    private final VocaRetriever vocaRetriever;

    public boolean existsByUserIdAndRecommendId(final Long userId, final Long recommendId) {
        return vocaRetriever.existsByUserIdAndRecommendId(userId, recommendId);
    }

}