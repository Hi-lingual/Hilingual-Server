package org.sopt.controller.diaryfeedback.dto;

import java.util.List;

public record DiaryFeedbackListRes(
        List<DiaryFeedbackDto> feedbackList
) {
    public record DiaryFeedbackDto(
            String original,
            String rewrite,
            String explain
    ){}
}