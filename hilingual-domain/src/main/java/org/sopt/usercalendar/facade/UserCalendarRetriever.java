package org.sopt.usercalendar.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.diary.domain.Diary;
import org.sopt.diary.repository.DiaryRepository;
import org.sopt.user.domain.User;
import org.sopt.usercalendar.domain.UserCalendar;
import org.sopt.usercalendar.exception.UserCalendarCoreErrorCode;
import org.sopt.usercalendar.exception.UserCalendarDiaryNotFoundException;
import org.sopt.usercalendar.dto.UserCalendarDiarySummaryRes;
import org.sopt.usercalendar.repository.UserCalendarRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserCalendarRetriever {

    private final DiaryRepository diaryRepository;
    private final UserCalendarRepository userCalendarRepository;

    public Optional<UserCalendar> findByUserAndDate(User user, LocalDate date) {
        return userCalendarRepository.findByUserAndDate(user, date);
    }

    boolean existsByUserAndDate(User user, LocalDate date){
        return userCalendarRepository.existsByUserAndDate(user, date);
    }

    public List<LocalDate> findWrittenDatesByMonth(final Long userId, final int year, final int month) {
        return userCalendarRepository.findWrittenDatesByUserIdAndYearAndMonth(userId, year, month);
    }

    public Diary findDiaryByDate(final Long userId, final LocalDate date) {
        return diaryRepository.findFirstByUserIdAndWrittenDate(userId, date)
                .orElseThrow(() -> new UserCalendarDiaryNotFoundException(UserCalendarCoreErrorCode.DIARY_NOT_FOUND));
    }

}
