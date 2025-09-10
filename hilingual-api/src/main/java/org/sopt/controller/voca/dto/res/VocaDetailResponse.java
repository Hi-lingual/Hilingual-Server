package org.sopt.controller.voca.dto.res;

import org.sopt.recommend.domain.Recommend;
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

    public static VocaDetailResponse of(Recommend r, boolean isBookmarked) {
        return new VocaDetailResponse(
                r.getId(),
                r.getPhrase(),
                parsePhraseTypes(r.getPhraseType()),
                r.getExplanation(),
                r.getReason(),
                isBookmarked
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
