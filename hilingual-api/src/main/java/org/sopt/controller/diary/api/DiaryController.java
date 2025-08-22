package org.sopt.controller.diary.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.diary.dto.CreateDiaryReq;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.controller.diary.dto.DiaryRes;
import org.sopt.controller.diary.exception.DiaryApiErrorCode;
import org.sopt.controller.diary.exception.DiaryContentTooShortException;
import org.sopt.controller.diary.service.DiaryService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<DiaryRes> createDiary(
            @UserId Long userId,
            @RequestBody CreateDiaryReq req
    ) {
        if (req.originalText() == null || req.originalText().length() < 10) {
            throw new DiaryContentTooShortException(DiaryApiErrorCode.DIARY_TOO_SHORT);
        }
        LocalDate writtenDate = LocalDate.parse(req.date());
        return ResponseEntity.ok(
                diaryService.createDiaryWithFeedback(userId, req.originalText(), writtenDate, req.image())
        );
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDetailsRes> getDiaryDetails(
            @UserId Long userId,
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ){
        return ResponseEntity.ok(diaryService.getDiaryDetails(userId, diaryId));
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> removeDiary(
            @UserId Long userId,
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ){
        diaryService.removeDairy(userId, diaryId);
        return ResponseEntity.ok().build();
    }

}
