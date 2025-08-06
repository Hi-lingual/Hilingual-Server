package org.sopt.controller.recommend.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.recommend.dto.BookmarkReq;
import org.sopt.controller.recommend.dto.RecommendListRes;
import org.sopt.controller.recommend.service.RecommendService;
import org.sopt.jwt.auth.util.UserAuthenticationUtils;
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
            @PathVariable @Validated @Min(1) Long diaryId
    ){
        Long userId = UserAuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(recommendService.getRecommendList(userId, diaryId));
    }

    @PatchMapping("/{phraseId}")
    public ResponseEntity<Void> bookMark(
            @PathVariable @Validated @Min(1) Long phraseId,
            @RequestBody @NotNull BookmarkReq bookmarkRequest
    ){
        Long userId = UserAuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(recommendService.bookMark(userId, phraseId, bookmarkRequest.isBookmarked()));
    }
}
