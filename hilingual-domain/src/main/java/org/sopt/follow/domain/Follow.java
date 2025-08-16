package org.sopt.follow.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.user.domain.User;

@Entity
@Table(name = FollowTableConstants.TABLE_FOLLOW)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FollowTableConstants.COLUMN_FOLLOW_ID)
    private Long followId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FollowTableConstants.COLUMN_FOLLOWER_ID, nullable = false)
    private User follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FollowTableConstants.COLUMN_FOLLOWEE_ID, nullable = false)
    private User followee;

    public static Follow create(User follower, User followee) {
        return new Follow(null, follower, followee);
    }
}
