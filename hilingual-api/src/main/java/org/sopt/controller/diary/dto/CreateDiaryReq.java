package org.sopt.controller.diary.dto;

public record CreateDiaryReq(
        String originalText,
        String date,
        ImageRef image
) {
    public record ImageRef(
            String fileKey,
            String purpose
    ) { }
}