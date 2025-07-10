package org.hilingual.domain.voca.api.dto.res;

import org.hilingual.domain.voca.core.domain.Voca;

import java.util.Arrays;
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
                parsePhraseTypes(voca.getRecommend().getPhraseType())
        );
    }

    private static List<String> parsePhraseTypes(String phraseTypeRaw) {
        if (phraseTypeRaw == null || phraseTypeRaw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(phraseTypeRaw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
