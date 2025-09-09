package org.sopt.usercalendar.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.user.domain.User;
import org.sopt.usercalendar.domain.UserCalendar;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserCalendarFacade {

    private final UserCalendarRetriever userCalendarRetriever;
    private final UserCalendarSaver userCalendarSaver;
    private final UserCalendarUpdater userCalendarUpdater;

    @Transactional
    public void markWrittenDate(User user, LocalDate writtenDate) {
        userCalendarRetriever.findByUserAndDate(user, writtenDate)
                .ifPresentOrElse(
                        UserCalendar::markWritten,
                        () -> userCalendarSaver.save(user, writtenDate)
                );
    }

    public Diary findDiaryByDate(Long userId, LocalDate date) {
        return userCalendarRetriever.findDiaryByDate(userId, date);
    }

    public List<LocalDate> findWrittenDatesByMonth(Long userId, int year, int month) {
        return userCalendarRetriever.findWrittenDatesByMonth(userId, year, month);
    }

    public boolean existsByUserAndDate(User user, LocalDate date) {
        return userCalendarRetriever.existsByUserAndDate(user, date);
    }

    @Transactional
    public void markDeleted(final Long userId, final LocalDate date) {
        userCalendarUpdater.markDeleted(userId, date);
    }

}
