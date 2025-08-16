package org.sopt.controller.block.service;

import lombok.RequiredArgsConstructor;
import org.sopt.block.facade.BlockFacade;
import org.sopt.controller.block.exception.BlockApiErrorCode;
import org.sopt.controller.block.exception.CannotSelfBlockException;
import org.sopt.controller.block.exception.CannotSelfUnblockException;
import org.sopt.jwt.auth.util.UserAuthenticationUtils;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BlockService {

    private final UserFacade userFacade;
    private final BlockFacade blockFacade;

    @Transactional
    public Void blockUser(Long blockerId, Long blockedId) {
        // 자기 자신인지 확인
        if(blockerId.equals(blockedId)) {
            throw new CannotSelfBlockException(BlockApiErrorCode.CANNOT_SELF_BLOCK);
        }

        Long firstId = Math.min(blockerId, blockedId);
        long secondId = Math.max(blockerId, blockedId);

        User firstUser = userFacade.getUserByIdWithLock(firstId);
        User secondUser = userFacade.getUserByIdWithLock(secondId);

        User blocker = (firstId.equals(blockerId)) ? firstUser : secondUser;
        User blocked = (firstId.equals(blockedId)) ? firstUser : secondUser;

        blockFacade.block(blocker, blocked);
        return null;
    }

    @Transactional
    public Void unblockUser(Long blockerId, Long unblockedId) {
        if(blockerId.equals(unblockedId)) {
            throw new CannotSelfUnblockException(BlockApiErrorCode.CANNOT_SELF_UNBLOCK);
        }

        User blocker = userFacade.getUserById(blockerId);
        User unblocked = userFacade.getUserById(unblockedId);

        blockFacade.unblock(blocker, unblocked);
        return null;
    }
}
