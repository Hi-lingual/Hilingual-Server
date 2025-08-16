package org.sopt.block.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.block.domain.Block;
import org.sopt.block.exception.AlreadyBlockedUserException;
import org.sopt.block.exception.BlockCoreErrorCode;
import org.sopt.block.repository.BlockRepository;
import org.sopt.user.domain.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BlockSaver {

    private final BlockRepository blockRepository;

    @Transactional
    public Block save(User blocker, User blocked) {
        try {
            return blockRepository.save(Block.create(blocker, blocked));
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyBlockedUserException(BlockCoreErrorCode.ALREADY_BLOCKED_USER);
        }
    }
}
