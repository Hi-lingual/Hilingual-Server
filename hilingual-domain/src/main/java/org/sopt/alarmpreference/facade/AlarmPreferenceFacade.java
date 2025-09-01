package org.sopt.alarmpreference.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.domain.AlarmPreference;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlarmPreferenceFacade {

    private final AlarmPreferenceSaver alarmPreferenceSaver;

    /*
     * Saver
     */
    public AlarmPreference save(AlarmPreference alarmPreference) {
        return alarmPreferenceSaver.save(alarmPreference);
    }
}
