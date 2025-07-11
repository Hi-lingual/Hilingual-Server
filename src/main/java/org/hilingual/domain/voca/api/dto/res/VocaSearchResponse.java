package org.hilingual.domain.voca.api.dto.res;

import org.hilingual.domain.voca.core.domain.Voca;

import java.util.Arrays;
import java.util.List;

public record VocaSearchResponse(
        Long phraseId,
        String phrase,
        List<String> phraseType
) {
    public static VocaSearchResponse from(final Voca voca) {
        return new VocaSearchResponse(
                voca.getRecommend().getId(),
                voca.getRecommend().getPhrase(),
                parsePhraseTypes(voca.getRecommend().getPhraseType())
        );
    }

    private static List<String> parsePhraseTypes(final String raw) {
        if (raw == null || raw.isBlank()) {
            return List.of();
        }
        return Arrays.stream(raw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}
