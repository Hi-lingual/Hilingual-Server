package org.sopt.noticedelivery.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.noticedelivery.domain.NoticeDelivery;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NoticeDeliveryFacade {

    private final NoticeDeliveryRetriever noticeDeliveryRetriever;

    public NoticeDelivery findByUserIdAndNoticeIdWithDetail(final long userId, final long noticeId){
        return noticeDeliveryRetriever.findByUserIdAndNoticeIdWithDetail(userId, noticeId);
    }

}