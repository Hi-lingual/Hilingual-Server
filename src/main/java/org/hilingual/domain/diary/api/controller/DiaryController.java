package org.hilingual.domain.diary.api.controller;

import lombok.RequiredArgsConstructor;
import org.hilingual.domain.diary.api.dto.res.DiaryDto;
import org.hilingual.domain.diary.api.exception.DiaryApiErrorCode;
import org.hilingual.domain.diary.api.exception.DiaryContentTooShortException;
import org.hilingual.domain.diary.api.service.DiaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping(value = "/v1/diaries", consumes = "multipart/form-data")
    public ResponseEntity<DiaryDto> getFeedbacks(
           // @RequestHeader("Authorization") Long userId,
            @RequestPart("originalText") String originalText,
            @RequestPart(value = "imageFile", required = false) MultipartFile imageFile
    ) {
        if(originalText.length() < 10){
            throw new DiaryContentTooShortException(DiaryApiErrorCode.DIARY_TOO_SHORT);
        }
        return ResponseEntity.ok(diaryService.getFeedbacks(1L, originalText, imageFile));
    }

}
