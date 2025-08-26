package org.sopt.controller.voca.dto.res;

import org.sopt.voca.domain.Voca;
import java.util.Arrays;
import java.util.List;

public record VocaDetailResponse(
        Long phraseId,
        String phrase,
        List<String> phraseType,
        String explanation,
        String writtenFrom,
        Boolean isBookmarked
) {
    public static VocaDetailResponse from(final Voca v) {
        return new VocaDetailResponse(
                v.getRecommendId(),
                v.getPhrase(),
                parsePhraseTypes(v.getPhraseType()),
                v.getExplanation(),
                v.getWrittenFrom(),
                true
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
