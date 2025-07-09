package org.hilingual.domain.diaryfeedback.api.controller;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diaryfeedback.api.dto.DiaryFeedbackList;
import org.hilingual.domain.diaryfeedback.api.service.DiaryFeedbackService;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class DiaryFeedbackController {

    private final DiaryFeedbackService diaryFeedbackService;

    @GetMapping("/v1/diaries/{diaryId}/feedbacks")
    public ResponseEntity<DiaryFeedbackList> getDiaryFeedbackList(
            @RequestHeader("Authorization") Long userId,
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ) {
        return ResponseEntity.ok(diaryFeedbackService.getFeedbackList(userId, diaryId));
    }
}