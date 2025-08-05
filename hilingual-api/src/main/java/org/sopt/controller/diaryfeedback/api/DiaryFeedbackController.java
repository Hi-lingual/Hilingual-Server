package org.sopt.controller.diaryfeedback.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.diaryfeedback.dto.DiaryFeedbackListRes;
import org.sopt.controller.diaryfeedback.service.DiaryFeedbackService;
import org.sopt.jwt.auth.util.UserAuthenticationUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
@Validated
public class DiaryFeedbackController {

    private final DiaryFeedbackService diaryFeedbackService;

    @GetMapping("/{diaryId}/feedbacks")
    public ResponseEntity<DiaryFeedbackListRes> getDiaryFeedbackList(
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ) {
        Long userId = UserAuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(diaryFeedbackService.getFeedbackList(userId, diaryId));
    }
}
