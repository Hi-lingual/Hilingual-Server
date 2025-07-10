package org.hilingual.domain.voca.api.dto.res;

import org.hilingual.domain.voca.core.domain.Voca;

import java.util.List;

public record VocaItemResponse(
        Long phraseId,
        String phrase,
        List<String> phraseType
) {
    public static VocaItemResponse from(final Voca voca) {
        return new VocaItemResponse(
                voca.getRecommend().getId(),
                voca.getRecommend().getPhrase(),
                List.of(voca.getRecommend().getPhraseType())
        );
    }
}
