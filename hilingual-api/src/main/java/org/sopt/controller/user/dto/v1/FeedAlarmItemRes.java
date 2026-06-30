package org.sopt.controller.user.dto.v1;

import org.sopt.feedalarm.domain.FeedAlarm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record FeedAlarmItemRes(
        Long noticeId,
        String type,
        String title,
        Long targetId,
        boolean isRead,
        String publishedAt,
        LocalDateTime publishedAtUtc
) implements NotificationItemRes {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static FeedAlarmItemRes from(FeedAlarm a) {
        return new FeedAlarmItemRes(
                a.getId(),
                a.getType().name(),
                a.getTitle(),
                a.getTargetId(),
                a.getReadAt() != null,
                a.getCreatedAt().toLocalDate().format(DATE_FORMATTER),
                a.getCreatedAt()
        );
    }
}