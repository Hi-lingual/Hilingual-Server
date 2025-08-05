package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.voca.repository.VocaRepository;
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