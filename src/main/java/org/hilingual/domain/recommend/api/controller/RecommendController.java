package org.hilingual.domain.recommend.api.controller;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.hilingual.domain.recommend.api.dto.req.BookmarkRequest;
import org.hilingual.domain.recommend.api.dto.res.RecommendList;
import org.hilingual.domain.recommend.api.service.RecommendService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static org.hilingual.auth.util.UserAuthenticationUtils.getCurrentUserId;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Validated
public class RecommendController {

    private final RecommendService recommendService;

    @GetMapping("/v1/diaries/{diaryId}/recommended")
    public ResponseEntity<RecommendList> getRecommendList(
            @PathVariable @Validated @Min(1) Long diaryId
    ){
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(recommendService.getRecommendList(userId, diaryId));
    }

    @PatchMapping("/v1/diaries/{phraseId}")
    public ResponseEntity<Void> bookMark(
            @PathVariable @Validated @Min(1) Long phraseId,
            @RequestBody @NotNull BookmarkRequest bookmarkRequest
    ){
        Long userId = getCurrentUserId();
        return ResponseEntity.ok(recommendService.bookMark(userId, phraseId, bookmarkRequest.isBookmarked()));
    }
}
