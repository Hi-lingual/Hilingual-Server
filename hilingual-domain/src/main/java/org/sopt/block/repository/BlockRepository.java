package org.sopt.block.repository;

import org.sopt.block.domain.Block;
import org.sopt.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {

    @Query("""
           SELECT CASE WHEN EXISTS (
               SELECT 1 FROM Block b
               WHERE b.blocker = :blocker AND b.blocked = :blocked
           ) THEN true ELSE false END
           """)
    boolean existsByBlockerAndBlocked(@Param("blocker") User blocker, @Param("blocked") User blocked);

    @Query("""
           SELECT CASE WHEN EXISTS (
               SELECT 1 FROM Block b
               WHERE b.blocker = :blocked AND b.blocked = :blocker
           ) THEN true ELSE false END
           """)
    boolean existsByReverseBlockerAndBlocked(@Param("blocker") User blocker, @Param("blocked") User blocked);

    @Query("""
           SELECT b FROM Block b
           WHERE b.blocker = :blocker AND b.blocked = :blocked
           """)
    Optional<Block> findByBlockerAndBlocked(@Param("blocker") User blocker,
                                            @Param("blocked") User blocked);

    @Query("""
           SELECT b.blocked.id
           FROM Block b WHERE b.blocker.id = :blockerId
           ORDER BY b.createdAt DESC
           """)
    List<Long> findBlockedUsers(@Param("blockerId") Long blockerId);

    // 내가 차단한 상대인지 확인
    boolean existsByBlockerIdAndBlockedId(Long blockerId, Long blockedId);
}
