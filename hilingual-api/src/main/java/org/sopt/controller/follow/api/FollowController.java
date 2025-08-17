package org.sopt.controller.follow.api;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.follow.dto.FollowerDtoRes;
import org.sopt.controller.follow.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feed/profiles")
public class FollowController {

    private final FollowService followService;

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowerDtoRes>> getFollowerList(
            @PathVariable Long userId
    ) {
        // TODO 수정
        Long mockUserId = 1L;
        return ResponseEntity.ok(followService.getFollowerList(mockUserId));
    }
}
