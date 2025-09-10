package org.sopt.block.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.block.repository.BlockRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BlockRemover {

    private final BlockRepository blockRepository;

    public void deleteAllByUserId(final long userId) {
        blockRepository.deleteAllByBlockerIdOrBlockedId(userId, userId);
    }
}
