package org.hilingual.domain.usercalendar.core.facade;

import lombok.RequiredArgsConstructor;
import org.hilingual.common.domain.Topic;
import org.hilingual.domain.diary.core.domain.Diary;
import org.hilingual.domain.diary.core.repository.DiaryRepository;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarDiarySummaryResponse;
import org.hilingual.domain.usercalendar.api.dto.res.UserCalendarTopicResponse;
import org.hilingual.domain.usercalendar.api.exception.UserCalendarDiaryNotFoundException;
import org.hilingual.domain.usercalendar.core.exception.UserCalendarCoreErrorCode;
import org.hilingual.domain.usercalendar.core.exception.UserCalendarTopicNotFoundException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class UserCalendarRetriever {

    private final DiaryRepository diaryRepository;
    private final org.hilingual.common.repository.TopicRepository topicRepository;

    public UserCalendarDiarySummaryResponse findDiaryByDate(final Long userId, final LocalDate date) {
        LocalDateTime startOfDay = date.atStartOfDay(); // 00:00
        LocalDateTime endOfDay = date.plusDays(1).atStartOfDay(); // 다음날 00:00

        return diaryRepository.findByUserIdAndCreatedAtBetween(userId, startOfDay, endOfDay)
                .stream()
                .findFirst()
                .map(d ->
                        UserCalendarDiarySummaryResponse.of(
                                d.getId(),
                                d.getCreatedAt(),
                                d.getImageUrl(),
                                d.getOriginalText()
                        )
                )
                .orElseThrow(() ->
                        new UserCalendarDiaryNotFoundException(UserCalendarCoreErrorCode.DIARY_NOT_FOUND)
                );
    }
    public UserCalendarTopicResponse findTopicByDate(final LocalDate date) {
        final Topic topic = topicRepository.findByDate(date)
                .orElseThrow(() -> new UserCalendarTopicNotFoundException(UserCalendarCoreErrorCode.TOPIC_NOT_FOUND));

        final int remainingTime = calculateRemainingMinutesUntilDeadline(date);

        return UserCalendarTopicResponse.of(topic.getTopicKor(), topic.getTopicEn(), remainingTime);
    }
    private int calculateRemainingMinutesUntilDeadline(final LocalDate topicDate) {
        final ZoneId seoulZone = ZoneId.of("Asia/Seoul");
        final LocalDateTime now = LocalDateTime.now(seoulZone);

        // 작성 가능 시각: 주제 날짜 + 2일 00시 = 48시간 후
        final LocalDateTime deadline = topicDate.plusDays(2).atStartOfDay();

        long secondsRemaining = Duration.between(now, deadline).getSeconds();

        if (secondsRemaining <= 0) {
            return 0; // 작성 불가
        } else if (secondsRemaining <= 60) {
            return 1; // 1초~60초 = 1분
        } else {
            return (int) Math.ceil(secondsRemaining / 60.0);
        }
    }

}
