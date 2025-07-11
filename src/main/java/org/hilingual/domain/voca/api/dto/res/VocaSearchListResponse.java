package org.hilingual.domain.voca.api.dto.res;

import org.hilingual.domain.voca.core.domain.Voca;

import java.util.Arrays;
import java.util.List;

public record VocaSearchListResponse(
        List<Item> searchList
) {
    public static VocaSearchListResponse from(final List<Voca> vocas) {
        return new VocaSearchListResponse(
                vocas.stream()
                        .map(Item::from)
                        .toList()
        );
    }

    public record Item(
            Long phraseId,
            String phrase,
            List<String> phraseType
    ) {
        public static Item from(final Voca voca) {
            return new Item(
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
}
