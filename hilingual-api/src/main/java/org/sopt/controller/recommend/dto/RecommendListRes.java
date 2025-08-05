package org.sopt.controller.recommend.dto;

import java.util.List;

public record RecommendListRes(
        List<PhraseDto> phraseList
) {
    public record PhraseDto(
            Long phraseId,
            List<String> phraseType,
            String phrase,
            String explanation,
            String reason,
            Boolean isBookmarked

    ){
    }
}