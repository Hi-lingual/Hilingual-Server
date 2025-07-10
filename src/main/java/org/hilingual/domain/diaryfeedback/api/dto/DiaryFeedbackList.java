package org.hilingual.domain.diaryfeedback.api.dto;

import java.util.List;

public record DiaryFeedbackList(
        List<DiaryFeedbackDto> feedbackList
) {
    public record DiaryFeedbackDto(
            String original,
            String rewrite,
            String explain
    ){}
}