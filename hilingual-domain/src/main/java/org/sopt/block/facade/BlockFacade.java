package org.sopt.block.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.block.domain.Block;
import org.sopt.block.exception.AlreadyBlockedUserException;
import org.sopt.block.exception.BlockCoreErrorCode;
import org.sopt.block.exception.BlockedNotFoundException;
import org.sopt.block.exception.UnblockableUserException;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BlockFacade {

    private final BlockRetriever blockRetriever;
    private final BlockSaver blockSaver;

    @Transactional
    public void block(User blocker, User blocked) {
        // 역방향 존재 확인 (상대가 이미 나를 차단했는지)
        if (blockRetriever.existsByReverseBlockerAndBlocked(blocker, blocked)) {
            throw new UnblockableUserException(BlockCoreErrorCode.UNBLOCKABLE_USER);
        }

        // 정방향 존재 확인 (이미 내가 차단했는지)
        if (blockRetriever.existsByBlockerAndBlocked(blocker, blocked)) {
            throw new AlreadyBlockedUserException(BlockCoreErrorCode.ALREADY_BLOCKED_USER);
        }

        blockSaver.save(blocker, blocked);
    }

    @Transactional
    public void unblock(User blocker, User unblocked) {
        Block relation = blockRetriever.findRelation(blocker, unblocked)
                .orElseThrow(() -> new BlockedNotFoundException(BlockCoreErrorCode.BLOCKED_NOT_FOUND));

        blockSaver.delete(relation);
    }
}
