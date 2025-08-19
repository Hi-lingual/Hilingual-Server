package org.sopt.controller.follow.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.follow.dto.NewFollowInfoRes;
import org.sopt.controller.follow.service.FollowService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users/following")
public class FollowController {

    private final FollowService followService;

    @PutMapping("/{targetUserId}")
    public ResponseEntity<Void> follow(
            @UserId Long userId,
            @PathVariable long targetUserId
    ){
        followService.follow(userId, targetUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<NewFollowInfoRes> unfollow(
            @UserId Long userId,
            @PathVariable long targetUserId
    ){
        return ResponseEntity.ok(followService.unfollow(userId, targetUserId));
    }

}