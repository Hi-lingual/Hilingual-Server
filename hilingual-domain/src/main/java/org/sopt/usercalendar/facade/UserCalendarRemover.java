package org.sopt.usercalendar.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.usercalendar.repository.UserCalendarRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCalendarRemover {
    private final UserCalendarRepository userCalendarRepository;

    public void deleteAllByUserId(Long userId) {
        userCalendarRepository.deleteAllByUserId(userId);
    };
}
