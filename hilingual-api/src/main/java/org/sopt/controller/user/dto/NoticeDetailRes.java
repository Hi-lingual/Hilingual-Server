package org.sopt.controller.user.dto;

import org.sopt.notice.domain.Notice;
import org.sopt.noticedetail.domain.NoticeDetail;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record NoticeDetailRes(
        String title,
        String createdAt,
        LocalDateTime createdAtUtc,
        String content
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static NoticeDetailRes from(Notice notice, NoticeDetail detail) {
        return new NoticeDetailRes(
                notice.getTitle(),
                notice.getCreatedAt().format(FORMATTER),
                notice.getCreatedAt(),
                detail.getContent()
        );
    }
}