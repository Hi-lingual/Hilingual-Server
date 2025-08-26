package org.sopt.voca.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.dto.VocaListRes;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VocaFacade {

    private final VocaRetriever vocaRetriever;
    private final VocaRemover vocaRemover;
    private final VocaSaver vocaSaver;

    public VocaListRes findGroupedVoca(final Long userId, final int sort) {
        return vocaRetriever.findGroupedVoca(userId, sort);
    }

    public List<Voca> findStartsWithVoca(final Long userId, final String keyword) {
        return vocaRetriever.findStartsWithVoca(userId, keyword);
    }

    public Voca findDetailByUserIdAndPhraseId(final Long userId, final Long phraseId){
        return vocaRetriever.findDetailByUserIdAndPhraseId(userId, phraseId);
    }
}