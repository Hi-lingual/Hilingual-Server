package org.sopt.controller.feed.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.feed.dto.FeedProfileRes;
import org.sopt.controller.feed.service.FeedService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed")
public class FeedController {

    private final FeedService feedService;

    @GetMapping("/profiles/{userId}")
    public ResponseEntity<FeedProfileRes> getFeedProfile(
            @PathVariable(value = "userId", required = false) Long targetUserId
    ) {
        Long userId = 1L;
        // TODO UserAuthenticationUtils.getCurrentUserId();
        // 본인의 프로필인 경우
        if (targetUserId == null) {
            targetUserId = 1L;
        }

        return ResponseEntity.ok(feedService.getFeedProfile(userId, targetUserId));
    }
}