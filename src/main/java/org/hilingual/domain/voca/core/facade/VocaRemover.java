package org.hilingual.domain.voca.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.recommend.core.domain.Recommend;
import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.voca.core.repository.VocaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VocaRemover {

    private final VocaRepository vocaRepository;

    @Transactional
    public void delete(final User user, final Recommend recommend) {
        vocaRepository.deleteByUserAndRecommend(user, recommend);
    }
}