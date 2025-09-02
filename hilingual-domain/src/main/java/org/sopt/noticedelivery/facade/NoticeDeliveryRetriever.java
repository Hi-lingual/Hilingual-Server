package org.sopt.noticedelivery.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.noticedelivery.domain.NoticeDelivery;
import org.sopt.noticedelivery.exception.NoticeDeliveryErrorCode;
import org.sopt.noticedelivery.exception.NoticeDeliveryNoFoundException;
import org.sopt.noticedelivery.repository.NoticeDeliveryRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticeDeliveryRetriever {

    private final NoticeDeliveryRepository noticeDeliveryRepository;

    public NoticeDelivery findByUserIdAndNoticeIdWithDetail(final long userId, final long noticeId){
        return noticeDeliveryRepository.findByUserIdAndNoticeIdWithDetail(userId, noticeId)
                .orElseThrow(() -> new NoticeDeliveryNoFoundException(NoticeDeliveryErrorCode.NOTICE_DELIVERY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<NoticeDelivery> findLatestByUserId(final long userId) {
        return noticeDeliveryRepository.findTop500ByUserIdOrderByDeliveredAtDesc(userId);
    }

}