package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.voca.domain.Voca;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VocaFacade {

    private final VocaRetriever vocaRetriever;

    public Voca findDetailByUserIdAndPhraseId(Long userId, Long phraseId){
        return vocaRetriever.findDetailByUserIdAndPhraseId(userId, phraseId);
    }

}