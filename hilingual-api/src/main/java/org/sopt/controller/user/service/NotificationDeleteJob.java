package org.sopt.controller.user.service;

import lombok.RequiredArgsConstructor;
import org.sopt.feedalarm.repository.FeedAlarmRepository;
import org.sopt.noticedelivery.repository.NoticeDeliveryRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class NotificationDeleteJob {

    private final FeedAlarmRepository feedRepo;
    private final NoticeDeliveryRepository noticeRepo;

    @Scheduled(cron = "0 0 3 * * *", zone = "Asia/Seoul")
    @Transactional
    public void trim() {
        int limit = 500;
        feedRepo.deleteAllUsersBeyondLimit(limit);
        noticeRepo.deleteAllUsersBeyondLimit(limit);
    }
}