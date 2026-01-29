package org.sopt.controller.feed.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.feed.dto.DiaryWriterProfileRes;
import org.sopt.controller.feed.dto.FeedProfileRes;
import org.sopt.controller.feed.dto.LikedDiaryListRes;
import org.sopt.controller.feed.dto.SharedDiaryListRes;
import org.sopt.controller.feed.dto.UserListRes;
import org.sopt.controller.feed.dto.*;
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
            @PathVariable(value = "targetUserId") Long targetUserId
    ) {
        if (targetUserId == 0) {
            targetUserId = userId;
        }

        return ResponseEntity.ok(feedService.getSharedDiaries(userId, targetUserId));
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

    @GetMapping("/search")
    public ResponseEntity<UserListRes> getUserList(
            @UserId Long userId,
            @RequestParam(value = "keyword") String keyword
    ) {
        return ResponseEntity.ok(feedService.getUserList(userId, keyword));
    }

    @GetMapping("/{diaryId}/users/profiles")
    public ResponseEntity<DiaryWriterProfileRes> getDiaryWriterProfile(
            @UserId Long userId,
            @PathVariable(value = "diaryId") @NotNull @Min(1) Long diaryId
    ) {
        return ResponseEntity.ok(feedService.getDiaryWriterProfile(userId, diaryId));
    }

    @GetMapping("/recommend")
    public ResponseEntity<RecommendFeedListRes> getRecommendFeed(
            @UserId Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(feedService.getRecommendFeeds(userId, page, size));
    }

    @GetMapping("/following")
    public ResponseEntity<FollowFeedListRes> getFollowFeed(
            @UserId Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size
    ) {
        return ResponseEntity.ok(feedService.getFollowFeeds(userId, page, size));
    }

    // 피드 좋아요 지정/해제 API
    @PostMapping("/likes/{diaryId}")
    public ResponseEntity<LikeToggleRes> toggleLike(
            @UserId Long userId,
            @PathVariable Long diaryId,
            @RequestBody LikeToggleReq req
    ) {
        return ResponseEntity.ok(feedService.toggleLike(userId, diaryId, req.isLiked()));
    }
}
