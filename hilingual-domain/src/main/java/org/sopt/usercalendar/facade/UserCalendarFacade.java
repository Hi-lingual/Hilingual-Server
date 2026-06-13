package org.sopt.usercalendar.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.domain.type.DiaryStatus;
import org.sopt.diary.facade.DiaryRetriever;
import org.sopt.recoveryticket.domain.RecoveryTicket;
import org.sopt.recoveryticket.facade.RecoveryTicketRetriever;
import org.sopt.user.domain.User;
import org.sopt.usercalendar.domain.UserCalendar;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.sopt.usercalendar.domain.WriteStatus;


import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCalendarFacade {

    private final UserCalendarRetriever userCalendarRetriever;
    private final UserCalendarSaver userCalendarSaver;
    private final UserCalendarUpdater userCalendarUpdater;
    private final UserCalendarRemover userCalendarRemover;
    private final DiaryRetriever diaryRetriever;
    private final RecoveryTicketRetriever recoveryTicketRetriever;

    @Transactional
    public void updateCalendarStatus(User user, LocalDate writtenDate, WriteStatus status) {
        userCalendarRetriever.findByUserAndDate(user, writtenDate)
                .ifPresentOrElse(
                        calendar -> calendar.updateStatus(status),
                        () -> userCalendarSaver.save(user, writtenDate, status)
                );
    }

    public Diary findDiaryByDate(Long userId, LocalDate date) {
        return userCalendarRetriever.findDiaryByDate(userId, date);
    }

    public Map<LocalDate, DiaryStatus> getMonthlyStatusMap(Long userId, int year, int month) {
        Map<LocalDate, DiaryStatus> statusMap = new HashMap<>();

        // 1. 일기 작성한 날짜 조회
        List<Diary> diaries = diaryRetriever.findDiariesByMonth(userId, year, month);
        for (Diary diary : diaries) {
            if (diary.getIsRecovered()) { // 기록 살리기로 작성한 일기인 경우
                statusMap.put(diary.getWrittenDate(), DiaryStatus.RECOVERED);
            } else { // 정상 작성한 일기인 경우
                statusMap.put(diary.getWrittenDate(), DiaryStatus.WRITTEN);
            }
        }

        // 2. 해금된 티켓 날짜 조회 - 광고 시청했으나 아직 일기를 작성하지 않은 경우
        List<RecoveryTicket> tickets = recoveryTicketRetriever.findUnlockedTicketsByMonth(userId, year, month);
        for (RecoveryTicket ticket : tickets) {
            // 이미 작성된 일기가 없는 경우(false)에만 UNLOCKED 상태
            statusMap.putIfAbsent(ticket.getWrittenDate(), DiaryStatus.UNLOCKED);
        }

        return statusMap;
    }

    public boolean existsByUserAndDate(User user, LocalDate date) {
        return userCalendarRetriever.existsByUserAndDate(user, date);
    }

    @Transactional
    public void markDeleted(final Long userId, final LocalDate date) {
        userCalendarUpdater.markDeleted(userId, date);
    }

    public void deleteAllByUserId(final Long userId) {
        userCalendarRemover.deleteAllByUserId(userId);
    }

    public Optional<UserCalendar> findByUserIdAndDate(final long userId, final LocalDate date) {
        return userCalendarRetriever.findByUserIdAndDate(userId, date);
    }

    public WriteStatus getStatus(User user, LocalDate date) {
        return userCalendarRetriever.findByUserAndDate(user, date)
                .map(UserCalendar::getStatus)
                .orElse(WriteStatus.NONE);
    }

    public WriteStatus getStatusByUserIdAndDate(long userId, LocalDate date) {
        return userCalendarRetriever.findByUserIdAndDate(userId, date)
                .map(UserCalendar::getStatus)
                .orElse(WriteStatus.NONE);
    }

    public boolean isWritten(User user, LocalDate date) {
        return getStatus(user, date) == WriteStatus.WRITTEN;
    }

    public long countWritten(final Long userId) {
        return userCalendarRetriever.countWrittenByUserId(userId);
    }

}
