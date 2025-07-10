package org.hilingual.domain.diary.api.dto.res;

import lombok.Builder;

import java.util.List;

@Builder
public record DiaryDetails(
        String date,
        String originalText,
        String rewriteText,
        List<DiffRange> diffRanges,
        String imageUrl
) {
    public record DiffRange(
            int start,
            int end,
            String correctedText
    ) {}
}