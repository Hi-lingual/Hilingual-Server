package org.sopt.controller.block.api;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.sopt.controller.block.dto.BlockedUserProfileDtoRes;
import org.sopt.controller.block.service.BlockService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class BlockController {

    private final BlockService blockService;

    @PutMapping("/{userId}/block")
    public ResponseEntity<Void> blockUser(
            @PathVariable("userId") @NotNull Long blockedId
    ) {
        Long blockerId = 1L;
        // TODO Long blockerId = UserAuthenticationUtils.getCurrentUserId();

        return ResponseEntity.ok(blockService.blockUser(blockerId, blockedId));
    }

    @DeleteMapping("/{userId}/unblock")
    public ResponseEntity<Void> unblockUser(
            @PathVariable("userId") @NotNull Long unblockedId
    ) {
        Long blockerId = 1L;
        // TODO Long blockerId = UserAuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(blockService.unblockUser(blockerId, unblockedId));
    }

    @GetMapping("/mypage/blocks")
    public ResponseEntity<List<BlockedUserProfileDtoRes>> getBlockedUserList(){
        Long userId = 1L;
        // TODO Long userId = UserAuthenticationUtils.getCurrentUserId();
        return ResponseEntity.ok(blockService.getBlockedUserList(userId));
    }
}
