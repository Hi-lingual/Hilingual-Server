package org.sopt.controller.diary.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.diaryfeedback.diff.dto.DiaryDetailsRes;
import org.sopt.controller.diary.dto.DiaryDtoRes;
import org.sopt.controller.diary.exception.DiaryApiErrorCode;
import org.sopt.controller.diary.exception.DiaryContentTooShortException;
import org.sopt.controller.diary.service.DiaryService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<DiaryDtoRes> getFeedbacks(
            @UserId Long userId,
            @RequestPart("originalText") String originalText,
            @RequestPart("date") String date,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        if(originalText.length() < 10){
            throw new DiaryContentTooShortException(DiaryApiErrorCode.DIARY_TOO_SHORT);
        }
        LocalDate writtenDate = LocalDate.parse(date);
        return ResponseEntity.ok(diaryService.getFeedbacks(userId, originalText, writtenDate, imageFile));
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryDetailsRes> getDiaryDetails(
            @UserId Long userId,
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ){
        return ResponseEntity.ok(diaryService.getDiaryDetails(userId, diaryId));
    }

}
