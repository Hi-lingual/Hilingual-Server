package org.sopt.controller.diary.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.annotation.UserTimezone;
import org.sopt.controller.diary.dto.CreateDiaryReq;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.controller.diary.dto.DiaryRes;
import org.sopt.controller.diary.service.DiaryService;
import org.sopt.jwt.annotation.UserId;
import org.sopt.web.UserZone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.ZoneId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    // 일기 피드백 요청 API
    @PostMapping
    public ResponseEntity<DiaryRes> createDiary(
            @UserId Long userId,
            @UserTimezone UserZone userZone,
            @Valid @RequestBody CreateDiaryReq req
    ) {
        LocalDate writtenDate = LocalDate.parse(req.date());
        return ResponseEntity.ok(
                diaryService.createDiaryWithFeedback(userId, req.originalText(), writtenDate, req.image(), userZone.zoneId())
        );
    }

    // [스트릭 부활 전용] 일기 피드백 요청 API
    @PostMapping("/recovery")
    public ResponseEntity<DiaryRes> createRecoveryDiary(
            @UserId Long userId,
            @UserTimezone UserZone userZone,
            @Valid @RequestBody CreateDiaryReq req
    ) {
        LocalDate writtenDate = LocalDate.parse(req.date());
        return ResponseEntity.ok(
                diaryService.createRecoveryDiaryWithFeedback(userId, req.originalText(), writtenDate, req.image(), userZone.zoneId())
        );
    }

    // AI가 교정한 일기 확인 API
    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDetailsRes> getDiaryDetails(
            @UserId Long userId,
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ){
        return ResponseEntity.ok(diaryService.getDiaryDetails(userId, diaryId));
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

    @PatchMapping("/{diaryId}/ad-watch")
    public ResponseEntity<Void> markAdWatched(
            @UserId Long userId,
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ){
        diaryService.markAdWatched(userId, diaryId);
        return ResponseEntity.ok().build();
    }

}
