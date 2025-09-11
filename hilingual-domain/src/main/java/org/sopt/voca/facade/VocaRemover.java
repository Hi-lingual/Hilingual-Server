package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.voca.repository.VocaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VocaRemover {

    private final VocaRepository vocaRepository;

    @Transactional
    public void delete(final long userId, final long recommendId) {
        vocaRepository.deleteByUserIdAndRecommendId(userId, recommendId);
    }

    public void deleteAllByUserId(final long userId) {
        vocaRepository.deleteAllByUserId(userId);
    }
}