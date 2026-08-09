package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.voca.domain.Voca;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VocaFacade {

    private final VocaRetriever vocaRetriever;
    private final VocaRemover vocaRemover;
    private final VocaUpdater vocaUpdater;

    public boolean existsByUserIdAndRecommendId(final Long userId, final Long recommendId) {
        return vocaRetriever.existsByUserIdAndRecommendId(userId, recommendId);
    }

    public Optional<Voca> findOptionalByUserIdAndRecommendId(final Long userId, final Long recommendId) {
        return vocaRetriever.findOptionalByUserIdAndRecommendId(userId, recommendId);
    }

    public void updateMemorization(final Long userId, final Map<Long, Boolean> memorizationByRecommendId){
        vocaUpdater.updateMemorization(userId, memorizationByRecommendId);
    }

    public void deleteAllByUserId(final long userId) {
        vocaRemover.deleteAllByUserId(userId);
    }

}