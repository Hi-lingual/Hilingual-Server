package org.hilingual.test;

import lombok.Builder;

@Builder
public record SuccessTestResponse(
        long diaryId,
        String title,
        String writer,
        String content
) {
}