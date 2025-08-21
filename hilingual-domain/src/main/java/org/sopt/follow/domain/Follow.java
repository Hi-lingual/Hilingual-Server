package org.sopt.follow.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.user.domain.User;

import static org.sopt.follow.domain.FollowTableConstants.*;

@Entity
@Table(
        name = TABLE_FOLLOW,
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = {
                                COLUMN_FOLLOWER_ID,
                                COLUMN_FOLLOWEE_ID
                        }
                )
        }
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_FOLLOW_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_FOLLOWER_ID, nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_FOLLOWEE_ID, nullable = false)
    private User followee;

    public static Follow create(User follower, User followee) {
        return new Follow(null, follower, followee);
    }
}
