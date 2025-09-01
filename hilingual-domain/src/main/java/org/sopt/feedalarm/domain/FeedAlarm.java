package org.sopt.feedalarm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.feedalarm.type.FeedAlarmType;
import org.sopt.feedalarm.type.FeedAlarmTypeConverter;
import org.sopt.feedalarm.type.TargetType;
import org.sopt.feedalarm.type.TargetTypeConverter;
import org.sopt.user.domain.User;

import java.time.LocalDateTime;
import static org.sopt.feedalarm.domain.FeedAlarmTableConstants.*;

@Entity
@Table(name = TABLE_FEED_ALARM)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FeedAlarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    private User user;

    @Convert(converter = FeedAlarmTypeConverter.class)
    @Column(name = COLUMN_TYPE, nullable = false)
    private FeedAlarmType type;

    @Convert(converter = TargetTypeConverter.class)
    @Column(name = COLUMN_TARGET_TYPE, nullable = false)
    private TargetType targetType;

    @Column(name = COLUMN_TARGET_ID, nullable = false)
    private Long targetId;

    @Column(name = COLUMN_ACTOR_ID, nullable = false)
    private Long actorId;

    @Column(name = COLUMN_TITLE, nullable = false, length = 100)
    private String title;

    @Column(name = COLUMN_READ_AT)
    private LocalDateTime readAt;

    public void markAsRead() {
        if (this.readAt == null) {
            this.readAt = LocalDateTime.now();
        }
    }

    public static FeedAlarm createFollowUser(User targetUser, Long actorId, String title) {
        return new FeedAlarm(
                null,
                targetUser,
                FeedAlarmType.FOLLOW_USER,
                TargetType.USER,
                actorId,
                actorId,
                title,
                null
        );
    }

}
