package org.sopt.controller.voca.dto.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VocaMemorizationUpdateRequest(
        @NotEmpty(message = "수정할 단어 목록은 비어 있을 수 없습니다.")
        List<@Valid Item> items
) {
    public record Item(
            @NotNull(message = "phraseId는 필수입니다.")
            Long phraseId,
            @NotNull(message = "isMemorized는 필수입니다.")
            Boolean isMemorized
    ) {}
}