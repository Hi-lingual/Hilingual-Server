package org.sopt.controller.recommend.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.recommend.dto.BookmarkReq;
import org.sopt.controller.recommend.dto.RecommendListRes;
import org.sopt.controller.recommend.service.RecommendService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diaries")
@Validated
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/{diaryId}/recommended")
    public ResponseEntity<RecommendListRes> getRecommendList(
            @UserId Long userId,
            @PathVariable @Validated @Min(1) Long diaryId
    ){
        return ResponseEntity.ok(recommendService.getRecommendList(userId, diaryId));
    }

    @PatchMapping("/{phraseId}")
    public ResponseEntity<Void> bookMark(
            @UserId Long userId,
            @PathVariable @Validated @Min(1) Long phraseId,
            @RequestBody @NotNull BookmarkReq bookmarkRequest
    ){
        return ResponseEntity.ok(recommendService.bookMark(userId, phraseId, bookmarkRequest.isBookmarked()));
    }
}
