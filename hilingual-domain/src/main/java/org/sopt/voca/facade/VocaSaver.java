package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recommend.domain.Recommend;
import org.sopt.user.domain.User;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.repository.VocaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class VocaSaver {

    private final VocaRepository vocaRepository;

    @Transactional
    public void saveIfNotExists(final User user, final Recommend recommend) {
        if (vocaRepository.existsByUserAndRecommend(user, recommend)) return;

        boolean mine = recommend.getDiary().getUser().getId().equals(user.getId());
        Voca voca = mine ? Voca.fromMyDiary(user, recommend)
                : Voca.fromFeed(user, recommend);

        vocaRepository.save(voca);
    }
}