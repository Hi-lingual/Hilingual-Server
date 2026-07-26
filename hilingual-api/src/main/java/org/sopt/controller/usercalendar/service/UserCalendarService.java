package org.sopt.controller.usercalendar.service;

import lombok.RequiredArgsConstructor;
import org.sopt.aws.s3.service.S3Service;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.domain.type.DiaryStatus;
import org.sopt.topic.facade.TopicFacade;
import org.sopt.usercalendar.dto.UserCalendarDiarySummaryRes;
import org.sopt.usercalendar.dto.UserCalendarTopicRes;
import org.sopt.usercalendar.facade.UserCalendarFacade;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserCalendarService {

    private final UserCalendarFacade userCalendarFacade;
    private final TopicFacade topicFacade;
    private final S3Service s3Service;

    public UserCalendarDiarySummaryRes getDiarySummary(final LocalDate date, final Long userId) {
        final Diary diary = userCalendarFacade.findDiaryByDate(userId, date);
        final String diaryImgUrl = s3Service.toPublicUrl(diary.getImageUrl());
        return UserCalendarDiarySummaryRes.of(diary, diaryImgUrl);

    }
    public UserCalendarTopicRes getTopicByDate(final long userId, final LocalDate targetDate, final ZoneId userZone) {
        return topicFacade.findTopicByDate(userId, targetDate, userZone);
    }

    @Transactional(readOnly = true)
    public Map<LocalDate, DiaryStatus> getMonthlyCalendarStatus(final Long userId, final int year, final int month) {
        return userCalendarFacade.getMonthlyStatusMap(userId, year, month);
    }

}
