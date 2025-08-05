package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.repository.VocaRepository;
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