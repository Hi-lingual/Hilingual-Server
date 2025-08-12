package org.sopt.alarm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = AlarmTableConstants.TABLE_FEED_ALARM)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FeedAlarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AlarmTableConstants.COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = AlarmTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = AlarmTableConstants.COLUMN_TYPE, nullable = false, length = 30)
    private Type type;

    @Enumerated(EnumType.STRING)
    @Column(name = AlarmTableConstants.COLUMN_TARGET_TYPE, nullable = false, length = 30)
    private TargetType targetType;

    @Column(name = AlarmTableConstants.COLUMN_TARGET_ID, nullable = false)
    private Long targetId;

    @Column(name = AlarmTableConstants.COLUMN_ACTOR_ID, nullable = false)
    private Long actorId;

    @Column(name = AlarmTableConstants.COLUMN_TITLE, nullable = false, length = 100)
    private String title;

    @Column(name = AlarmTableConstants.COLUMN_CREATED_AT, nullable = false)
    private LocalDateTime createdAt;

    @Column(name = AlarmTableConstants.COLUMN_READ_AT)
    private LocalDateTime readAt;

    public enum Type {
        LIKE_DIARY,
        FOLLOW_USER
    }

    public enum TargetType {
        DIARY,
        USER
    }
}
