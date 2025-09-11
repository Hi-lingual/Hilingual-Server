package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.voca.domain.Voca;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VocaFacade {

    private final VocaRetriever vocaRetriever;
    private final VocaRemover vocaRemover;

    public boolean existsByUserIdAndRecommendId(final Long userId, final Long recommendId) {
        return vocaRetriever.existsByUserIdAndRecommendId(userId, recommendId);
    }

    public Optional<Voca> findOptionalByUserIdAndRecommendId(final Long userId, final Long recommendId) {
        return vocaRetriever.findOptionalByUserIdAndRecommendId(userId, recommendId);
    }

    public void deleteAllByUserId(final long userId) {
        vocaRemover.deleteAllByUserId(userId);
    }

}