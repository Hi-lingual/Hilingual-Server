package org.sopt.controller.feed.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.feed.dto.DiaryWriterProfileRes;
import org.sopt.controller.feed.dto.FeedProfileRes;
import org.sopt.controller.feed.dto.LikedDiaryListRes;
import org.sopt.controller.feed.dto.SharedDiaryListRes;
import org.sopt.controller.feed.service.FeedService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed")
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/profiles/{targetUserId}")
    public ResponseEntity<FeedProfileRes> getFeedProfile(
            @UserId Long userId,
            @PathVariable(value = "targetUserId") @NotNull Long targetUserId
    ) {
        if (targetUserId == 0) {
            targetUserId = userId;
        }

        return ResponseEntity.ok(feedService.getFeedProfile(userId, targetUserId));
    }

    @GetMapping("/profiles/{targetUserId}/diaries/shared")
    public ResponseEntity<SharedDiaryListRes> getSharedDiaries(
            @UserId Long userId,
            @PathVariable(value = "targetUserId") @NotNull Long targetUserId
    ) {
        if (targetUserId == 0) {
            targetUserId = userId;
        }

        return ResponseEntity.ok(feedService.getSharedDiaries(targetUserId));
    }

    @GetMapping("/profiles/{targetUserId}/diaries/liked")
    public ResponseEntity<LikedDiaryListRes> getLikedDiaries(
            @UserId Long userId,
            @PathVariable(value = "targetUserId") @NotNull Long targetUserId
    ) {
        if (targetUserId == 0) {
            targetUserId = userId;
        }

        return ResponseEntity.ok(feedService.getLikedDiaries(targetUserId));
    }


    @GetMapping("/{diaryId}/users/profiles")
    public ResponseEntity<DiaryWriterProfileRes> getDiaryWriterProfile(
            @UserId Long userId,
            @PathVariable(value = "diaryId") @NotNull @Min(1) Long diaryId
    ) {
        return ResponseEntity.ok(feedService.getDiaryWriterProfile(userId, diaryId));
    }
}