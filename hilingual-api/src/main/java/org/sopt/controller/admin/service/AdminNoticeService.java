package org.sopt.controller.admin.service;

import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.repository.AlarmPreferenceRepository;
import org.sopt.alarmpreference.type.AlarmType;
import org.sopt.controller.admin.dto.NoticeCreateReq;
import org.sopt.controller.admin.dto.NoticeCreateRes;
import org.sopt.controller.admin.exception.AdminApiErrorCode;
import org.sopt.controller.admin.exception.NoticeBadRequestException;
import org.sopt.notice.domain.Notice;
import org.sopt.notice.exception.NoticeCoreErrorCode;
import org.sopt.notice.exception.NoticeInactiveException;
import org.sopt.notice.repository.NoticeRepository;
import org.sopt.notice.type.Category;
import org.sopt.noticedelivery.domain.NoticeDelivery;
import org.sopt.noticedelivery.domain.NoticeDeliveryTableConstants;
import org.sopt.noticedelivery.exception.NoticeAlreadyDeliveredException;
import org.sopt.noticedelivery.exception.NoticeDeliveryCoreErrorCode;
import org.sopt.noticedelivery.exception.NoticeNotFoundException;
import org.sopt.noticedelivery.repository.NoticeDeliveryRepository;
import org.sopt.user.domain.User;
import org.sopt.user.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class AdminNoticeService {

    private final NoticeRepository noticeRepository;
    private final UserRepository userRepository;
    private final AlarmPreferenceRepository alarmPreferenceRepository;
    private final NoticeDeliveryRepository noticeDeliveryRepository;

    private static final int BATCH_SIZE = 500;

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

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deliver(Long noticeId) {
        // 공지 확인
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new NoticeNotFoundException(NoticeCoreErrorCode.NOTICE_NOT_FOUND));

        // 비활성 공지면 발송 X
        if (Boolean.FALSE.equals(notice.getIsActive())) {
            throw new NoticeInactiveException(NoticeCoreErrorCode.NOTICE_INACTIVE);
        }

        // 이미 발송된 공지는 재발송 X
        if (noticeDeliveryRepository.existsByNoticeId(noticeId)) {
            throw new NoticeAlreadyDeliveredException(NoticeDeliveryCoreErrorCode.NOTICE_ALREADY_DELIVERED);
        }

        // 발송 대상 유저 식별
        List<Long> targetUserIds = switch (notice.getCategory()) {
            case SYSTEM -> userRepository.findAllActiveUserIds();
            case MARKETING -> alarmPreferenceRepository.findEnabledUserIdsByType(AlarmType.MARKETING);
        };

        if (targetUserIds.isEmpty()) {
            return; // 발송 대상 없음 → 바로 종료
        }

        LocalDateTime now = LocalDateTime.now();

        for (int i = 0; i < targetUserIds.size(); i += BATCH_SIZE) {
            int end = Math.min(i + BATCH_SIZE, targetUserIds.size());
            List<Long> batch = targetUserIds.subList(i, end);

            List<NoticeDelivery> rows = batch.stream()
                    .map(uid -> NoticeDelivery.create(notice, User.ref(uid), now))
                    .toList();

            try {
                noticeDeliveryRepository.saveAll(rows);
            } catch (DataIntegrityViolationException e) {
                if (!isUniqueViolation(e, NoticeDeliveryTableConstants.UK_NOTICE_DELIVERY_NOTICE_USER)) {
                    throw e;
                }
            }
            userRepository.turnOnNotifyByIds(batch);
        }
    }

    private boolean isUniqueViolation(DataIntegrityViolationException e, String constraintName) {
        Throwable cause = e.getMostSpecificCause();
        String msg = (cause != null ? cause.getMessage() : null);
        return msg != null && msg.contains(constraintName);
    }

}