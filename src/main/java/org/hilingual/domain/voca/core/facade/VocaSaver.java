package org.hilingual.domain.voca.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.voca.core.domain.Voca;
import org.hilingual.domain.voca.core.repository.VocaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VocaSaver {

    private final VocaRepository vocaRepository;

    @Transactional
    public void saveIfNotExists(final Voca voca) {
        boolean exists = vocaRepository.existsByUserAndRecommend(
                voca.getUser(),
                voca.getRecommend()
        );

        if (!exists) {
            vocaRepository.save(voca);
        }
    }
}