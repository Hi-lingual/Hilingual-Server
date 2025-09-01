package org.sopt.alarmpreference.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.domain.AlarmPreference;
import org.sopt.alarmpreference.repository.AlarmPreferenceRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmPreferenceSaver {
    private final AlarmPreferenceRepository alarmPreferenceRepository;

    public AlarmPreference save(AlarmPreference alarmPreference) {
        return alarmPreferenceRepository.save(alarmPreference);
    }
}
