package org.sopt.controller.admin.dto;

public record NoticeCreateRes(Long noticeId) {

    public static NoticeCreateRes of(Long id) {
        return new NoticeCreateRes(id);
    }

}
