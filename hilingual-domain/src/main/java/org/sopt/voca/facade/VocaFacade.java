package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.voca.domain.Voca;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VocaFacade {

    private final VocaRetriever vocaRetriever;
    private final VocaRemover vocaRemover;
    private final VocaSaver vocaSaver;

    public Voca findDetailByUserIdAndPhraseId(final Long userId, final Long phraseId){
        return vocaRetriever.findDetailByUserIdAndPhraseId(userId, phraseId);
    }
}