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

@Entity
@Table(name = FeedAlarmTableConstants.TABLE_FEED_ALARM)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FeedAlarm extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = FeedAlarmTableConstants.COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = FeedAlarmTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    @Convert(converter = FeedAlarmTypeConverter.class)
    @Column(name = FeedAlarmTableConstants.COLUMN_TYPE, nullable = false)
    private FeedAlarmType type;

    @Convert(converter = TargetTypeConverter.class)
    @Column(name = FeedAlarmTableConstants.COLUMN_TARGET_TYPE, nullable = false)
    private TargetType targetType;

    @Column(name = FeedAlarmTableConstants.COLUMN_TARGET_ID, nullable = false)
    private Long targetId;

    @Column(name = FeedAlarmTableConstants.COLUMN_ACTOR_ID, nullable = false)
    private Long actorId;

    @Column(name = FeedAlarmTableConstants.COLUMN_TITLE, nullable = false, length = 100)
    private String title;

    @Column(name = FeedAlarmTableConstants.COLUMN_READ_AT)
    private LocalDateTime readAt;
}
