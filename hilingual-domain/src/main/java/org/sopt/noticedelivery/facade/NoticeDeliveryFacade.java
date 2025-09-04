package org.sopt.noticedelivery.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.noticedelivery.domain.NoticeDelivery;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class NoticeDeliveryFacade {

    private final NoticeDeliveryRetriever noticeDeliveryRetriever;

    public NoticeDelivery findByUserIdAndNoticeIdWithDetail(final long userId, final long noticeId){
        return noticeDeliveryRetriever.findByUserIdAndNoticeIdWithDetail(userId, noticeId);
    }

    public List<NoticeDelivery> findLatestByUserId(final long userId) {
        return noticeDeliveryRetriever.findLatestByUserId(userId);
    }

}