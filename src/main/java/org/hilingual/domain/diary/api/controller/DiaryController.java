package org.hilingual.domain.diary.api.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.api.dto.res.DiaryDetails;
import org.hilingual.domain.diary.api.dto.res.DiaryDto;
import org.hilingual.domain.diary.api.exception.DiaryApiErrorCode;
import org.hilingual.domain.diary.api.exception.DiaryContentTooShortException;
import org.hilingual.domain.diary.api.service.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.hilingual.auth.util.UserAuthenticationUtils.getCurrentUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping(value = "/v1/diaries", consumes = "multipart/form-data")
    public ResponseEntity<DiaryDto> getFeedbacks(
            @RequestPart("originalText") String originalText,
            @RequestPart("date") String date,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        Long userId = getCurrentUserId();
        if(originalText.length() < 10){
            throw new DiaryContentTooShortException(DiaryApiErrorCode.DIARY_TOO_SHORT);
        }
        LocalDate writtenDate = LocalDate.parse(date);
        return ResponseEntity.ok(diaryService.getFeedbacks(userId, originalText, writtenDate, imageFile));
    }

    @GetMapping("/v1/diaries/{diaryId}")
    public ResponseEntity<DiaryDetails> getDiaryDetails(
            @PathVariable("diaryId") @NotNull @Min(1) Long diaryId
    ){
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(diaryService.getDiaryDetails(userId, diaryId));
    }

}
