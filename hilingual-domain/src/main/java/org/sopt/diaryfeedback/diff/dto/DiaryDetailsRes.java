package org.sopt.diaryfeedback.diff.dto;

import lombok.Builder;
import java.util.List;

@Builder
public record DiaryDetailsRes(
        String date,
        String originalText,
        String rewriteText,
        List<DiffRange> diffRanges,
        String imageUrl,
        Boolean isPublished
) {
    public record DiffRange(
            int start,
            int end,
            String correctedText
    ) {}
}