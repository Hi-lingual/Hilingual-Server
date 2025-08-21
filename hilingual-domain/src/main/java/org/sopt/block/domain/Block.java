package org.sopt.block.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.user.domain.User;

import static org.sopt.block.domain.BlockTableConstants.*;

@Entity
@Table(
        name = TABLE_BLOCK,
        uniqueConstraints = {
        @UniqueConstraint(
                name = UK_BLOCKER_BLOCKED,
                columnNames = {
                        COLUMN_BLOCKER_ID,
                        COLUMN_BLOCKED_ID
                }
        )
}
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Block extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_BLOCK_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_BLOCKER_ID, nullable = false)
    private User blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_BLOCKED_ID, nullable = false)
    private User blocked;

    public static Block create(User blocker, User blocked) {
        return new Block(null, blocker, blocked);
    }
}
