package org.sopt.controller.voca.dto.res;

import org.sopt.recommend.domain.Recommend;
import org.sopt.voca.domain.Voca;
import org.sopt.voca.type.SavedRoot;

import java.time.format.DateTimeFormatter;
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
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yy.MM.dd");
    private static final String FROM_FEED = "피드에서 저장됨";

    public static VocaDetailResponse from(final Voca voca) {
        Recommend r = voca.getRecommend();

        String writtenFrom = (voca.getSavedRoot() == SavedRoot.MY)
                ? r.getDiary().getWrittenDate().format(FORMATTER) + " 일기에서 저장됨"
                : FROM_FEED;

        return new VocaDetailResponse(
                r.getId(),
                r.getPhrase(),
                parsePhraseTypes(r.getPhraseType()),
                r.getExplanation(),
                writtenFrom,
                true
        );
    }

    private static List<String> parsePhraseTypes(String phraseTypeRaw) {
        if (phraseTypeRaw == null || phraseTypeRaw.isBlank()) return List.of();
        return Arrays.stream(phraseTypeRaw.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }
}