package org.hilingual.domain.voca.api.dto.res;

import org.hilingual.domain.voca.core.domain.Voca;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public record VocaDetailResponse(
        Long phraseId,
        String phrase,
        List<String> phraseType,
        String explanation,
        String createdAt,
        Boolean isBookmarked
) {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yy.MM.dd");

    public static VocaDetailResponse from(final Voca voca) {
        return new VocaDetailResponse(
                voca.getRecommend().getId(),
                voca.getRecommend().getPhrase(),
                parsePhraseTypes(voca.getRecommend().getPhraseType()),
                voca.getRecommend().getExplanation(),
                voca.getCreatedAt().format(FORMATTER),
                voca.getRecommend().getIsBookmarked()
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
