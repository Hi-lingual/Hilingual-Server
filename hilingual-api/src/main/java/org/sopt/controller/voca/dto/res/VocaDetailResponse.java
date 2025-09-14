package org.sopt.controller.voca.dto.res;

import org.sopt.recommend.domain.Recommend;
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
    // Recommend 기반
    public static VocaDetailResponse of(Recommend r, String writtenFrom, boolean isBookmarked) {
        return new VocaDetailResponse(
                r.getId(),
                r.getPhrase(),
                parsePhraseTypes(r.getPhraseType()),
                r.getExplanation(),
                writtenFrom,
                isBookmarked
        );
    }

    // Voca 스냅샷 기반
    public static VocaDetailResponse ofSnapshot(Voca v, boolean isBookmarked) {
        return new VocaDetailResponse(
                v.getRecommendId(),
                v.getPhrase(),
                parsePhraseTypes(v.getPhraseType()),
                v.getExplanation(),
                v.getWrittenFrom(),
                isBookmarked
        );
    }

    private static List<String> parsePhraseTypes(String raw) {
        if (raw == null || raw.isBlank()) return List.of();
        return Arrays.stream(raw.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }
}