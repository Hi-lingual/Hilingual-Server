package org.sopt.controller.follow.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.follow.dto.NewFollowInfoRes;
import org.sopt.controller.follow.service.FollowService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed")
public class FollowController {

    private final FollowService followService;

    @PutMapping("/{followId}/follow")
    public ResponseEntity<Void> follow(
            @UserId Long userId,
            @PathVariable long followId
    ){
        followService.follow(userId, followId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{unfollowId}/unfollow")
    public ResponseEntity<NewFollowInfoRes> unfollow(
            @UserId Long userId,
            @PathVariable long unfollowId
    ){
        return ResponseEntity.ok(followService.unfollow(userId, unfollowId));
    }

}