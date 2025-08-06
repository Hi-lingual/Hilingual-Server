package org.sopt.controller.test.dto;

import lombok.Builder;

@Builder
public record SuccessTestResponse(
        long diaryId,
        String title,
        String writer,
        String content
) {
}