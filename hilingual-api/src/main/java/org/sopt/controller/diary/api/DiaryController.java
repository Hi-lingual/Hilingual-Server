package org.sopt.controller.diary.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.diary.dto.CreateDiaryReq;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.controller.diary.dto.DiaryRes;
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
            @Valid @RequestBody CreateDiaryReq req
    ) {
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

    @PatchMapping("/{diaryId}/publish")
    public ResponseEntity<Void> publishDiary(
            @UserId Long userId,
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ){
        diaryService.publishDiary(userId, diaryId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{diaryId}/unpublish")
    public ResponseEntity<Void> unpublishDiary(
            @UserId Long userId,
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ){
        diaryService.unpublishDiary(userId, diaryId);
        return ResponseEntity.ok().build();
    }

}
