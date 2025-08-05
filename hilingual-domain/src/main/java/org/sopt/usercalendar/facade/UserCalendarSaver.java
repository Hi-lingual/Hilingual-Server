package org.sopt.usercalendar.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.user.domain.User;
import org.sopt.usercalendar.domain.UserCalendar;
import org.sopt.usercalendar.repository.UserCalendarRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserCalendarSaver {

    private final UserCalendarRepository userCalendarRepository;

    public void save(User user, LocalDate writtenDate) {
        UserCalendar newCalendar = UserCalendar.create(writtenDate, true, user);
        userCalendarRepository.save(newCalendar);
    }
}
