package org.sopt.controller.follow.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.follow.dto.NewFollowInfoRes;
import org.sopt.controller.follow.service.FollowService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.sopt.controller.follow.dto.FollowerListDtoRes;
import org.sopt.controller.follow.dto.FollowingListDtoRes;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{userId}/followers")
    public ResponseEntity<FollowerListDtoRes> getFollowerList(
            @PathVariable Long userId
    ) {
        // TODO 수정
        Long mockUserId = 1L;
        return ResponseEntity.ok(followService.getFollowerList(mockUserId));
    }

    @GetMapping("/{userId}/followings")
    public ResponseEntity<FollowingListDtoRes> getFollowingList(
            @PathVariable Long userId
    ) {
        // TODO 수정
        Long mockUserId = 1L;
        return ResponseEntity.ok(followService.getFollowingList(mockUserId));
    }
}