package org.sopt.alarmpreference.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.repository.AlarmPreferenceRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmPreferenceRemover {

    private final AlarmPreferenceRepository alarmPreferenceRepository;

    public void deleteAllByUserId(final long userId) {
        alarmPreferenceRepository.deleteAllByUserId(userId);
    }
}
