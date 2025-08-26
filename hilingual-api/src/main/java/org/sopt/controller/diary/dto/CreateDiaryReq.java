package org.sopt.controller.diary.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.sopt.aws.s3.dto.Purpose;

public record CreateDiaryReq(
        @NotNull(message = "원문은 필수입니다.")
        @Size(min = 10, max = 1000, message = "원문은 10~1000자여야 합니다.")
        String originalText,

        @NotNull(message = "작성일은 필수입니다.")
        String date,
        @Nullable ImageRef image
) {
    public record ImageRef(
            @NotNull String fileKey,
            @NotNull Purpose purpose
    ) { }
}