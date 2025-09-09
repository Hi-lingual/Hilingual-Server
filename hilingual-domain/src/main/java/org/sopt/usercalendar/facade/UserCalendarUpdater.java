package org.sopt.usercalendar.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.usercalendar.domain.WriteStatus;
import org.sopt.usercalendar.repository.UserCalendarRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class UserCalendarUpdater {

    private final UserCalendarRepository userCalendarRepository;

    @Transactional
    public void markDeleted(Long userId, LocalDate date) {
        userCalendarRepository.updateStatusByUserAndDate(
                userId, date, WriteStatus.DELETED
        );
    }

}