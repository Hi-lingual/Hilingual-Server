package org.sopt.block.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.block.domain.Block;
import org.sopt.block.repository.BlockRepository;
import org.sopt.user.domain.User;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class BlockRetriever {

    private final BlockRepository blockRepository;

    public boolean existsByBlockerAndBlocked(User blocker, User blocked) {
        return blockRepository.existsByBlockerAndBlocked(blocker, blocked);
    }

    public boolean existsByReverseBlockerAndBlocked(User blocker, User blocked) {
        return blockRepository.existsByReverseBlockerAndBlocked(blocker, blocked);
    }

    public Optional<Block> findRelation(User blocker, User blocked) {
        return blockRepository.findByBlockerAndBlocked(blocker, blocked);
    }

    public List<User> findBlockedUsers(Long blockerId) {
        return blockRepository.findBlockedUsers(blockerId);
    }
}
