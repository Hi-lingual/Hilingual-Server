package org.hilingual.domain.recommend.api.dto.res;

import java.util.List;

public record RecommendList(
        List<PhraseDto> phraseList
) {
    public record PhraseDto(
            Long phraseId,
            List<String> phraseType,
            String phrase,
            String explanation,
            String reason,
            Boolean isMarked

    ){
    }
}