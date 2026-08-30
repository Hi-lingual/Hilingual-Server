package org.sopt.controller.block.api;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.block.dto.BlockedListRes;
import org.sopt.controller.block.service.BlockService;
import org.sopt.jwt.annotation.UserId;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class BlockController {

    private final BlockService blockService;

    @PutMapping("/block/{targetUserId}")
    public ResponseEntity<Void> blockUser(
            @UserId Long blockerId,
            @PathVariable("targetUserId") @NotNull Long blockedId
    ) {
        return ResponseEntity.ok(blockService.blockUser(blockerId, blockedId));
    }

    @DeleteMapping("/unblock/{targetUserId}")
    public ResponseEntity<Void> unblockUser(
            @UserId Long blockerId,
            @PathVariable("targetUserId") @NotNull Long unblockedId
    ) {
        return ResponseEntity.ok(blockService.unblockUser(blockerId, unblockedId));
    }

    @GetMapping("/mypage/blocks")
    public ResponseEntity<BlockedListRes> getBlockedUserList(
            @UserId Long userId
    ){
        return ResponseEntity.ok(blockService.getBlockedUserList(userId));
    }
}
