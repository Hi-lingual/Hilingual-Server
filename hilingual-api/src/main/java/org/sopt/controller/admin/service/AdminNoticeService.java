package org.sopt.controller.admin.service;

import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.sopt.controller.admin.dto.NoticeCreateReq;
import org.sopt.controller.admin.dto.NoticeCreateRes;
import org.sopt.controller.admin.exception.AdminApiErrorCode;
import org.sopt.controller.admin.exception.NoticeBadRequestException;
import org.sopt.notice.domain.Notice;
import org.sopt.notice.repository.NoticeRepository;
import org.sopt.notice.type.Category;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AdminNoticeService {

    private final NoticeRepository noticeRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public NoticeCreateRes create(NoticeCreateReq req) {
        Category category;
        try {
            category = Category.valueOf(req.category().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new NoticeBadRequestException(AdminApiErrorCode.INVALID_CATEGORY);
        }

        Notice notice = Notice.create(category, req.title().trim(), req.content().trim());
        Notice saved = noticeRepository.save(notice);
        return NoticeCreateRes.of(saved.getId());
    }
}