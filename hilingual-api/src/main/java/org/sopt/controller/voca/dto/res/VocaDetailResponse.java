package org.sopt.controller.voca.dto.res;

import org.sopt.recommend.domain.Recommend;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public record VocaDetailResponse(
        Long phraseId,
        String phrase,
        List<String> phraseType,
        String explanation,
        String writtenDate,
        Boolean isBookmarked
) {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yy.MM.dd");

    public static VocaDetailResponse from(final Recommend recommend) {
        return new VocaDetailResponse(
                recommend.getId(),
                recommend.getPhrase(),
                parsePhraseTypes(recommend.getPhraseType()),
                recommend.getExplanation(),
                recommend
                        .getDiary()
                        .getWrittenDate()
                        .format(FORMATTER),
                recommend.getIsBookmarked()
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
