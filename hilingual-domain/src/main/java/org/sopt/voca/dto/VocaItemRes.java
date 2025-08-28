package org.sopt.voca.dto;

import org.sopt.voca.domain.Voca;

import java.util.Arrays;
import java.util.List;

public record VocaItemRes(
        Long phraseId,
        String phrase,
        List<String> phraseType,
        Boolean isBookmarked
) {
    public static VocaItemRes from(final Voca voca) {
        return new VocaItemRes(
                voca.getRecommendId(),
                voca.getPhrase(),
                parsePhraseTypes(voca.getPhraseType()),
                Boolean.TRUE
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