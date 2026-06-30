package org.sopt.controller.user.dto.v1;

import org.sopt.noticedelivery.domain.NoticeDelivery;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record NoticeListItemRes(
        Long noticeId,
        String category,
        String title,
        boolean isRead,
        String publishedAt,
        LocalDateTime publishedAtUtc
) implements NotificationItemRes {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static NoticeListItemRes from(NoticeDelivery d) {
        var notice = d.getNotice();

        return new NoticeListItemRes(
                notice.getId(),
                notice.getCategory().name(),
                notice.getTitle(),
                d.getReadAt() != null,
                notice.getCreatedAt().toLocalDate().format(DATE_FORMATTER),
                notice.getCreatedAt()
        );
    }
}