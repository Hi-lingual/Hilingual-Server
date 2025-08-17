package org.sopt.block.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.user.domain.User;

@Entity
@Table(
        name = BlockTableConstants.TABLE_BLOCK,
        uniqueConstraints = {
        @UniqueConstraint(
                columnNames = {
                        BlockTableConstants.COLUMN_BLOCKER_ID,
                        BlockTableConstants.COLUMN_BLOCKED_ID
                }
        )
}
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Block {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = BlockTableConstants.COLUMN_BLOCK_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = BlockTableConstants.COLUMN_BLOCKER_ID, nullable = false)
    private User blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = BlockTableConstants.COLUMN_BLOCKED_ID, nullable = false)
    private User blocked;

    public static Block create(User blocker, User blocked) {
        return new Block(null, blocker, blocked);
    }
}
