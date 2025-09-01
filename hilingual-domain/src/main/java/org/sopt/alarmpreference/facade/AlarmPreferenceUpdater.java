package org.sopt.alarmpreference.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.domain.AlarmPreference;
import org.sopt.alarmpreference.exception.AlarmPreferenceCoreErrorCode;
import org.sopt.alarmpreference.exception.NotFoundAlarmPreferenceRow;
import org.sopt.alarmpreference.repository.AlarmPreferenceRepository;
import org.sopt.alarmpreference.type.AlarmType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AlarmPreferenceUpdater {

    private final AlarmPreferenceRepository alarmPreferenceRepository;

    @Transactional
    public void toggle(final long userId, final AlarmType type) {
        AlarmPreference pref = alarmPreferenceRepository.findByUserIdAndAlarmType(userId, type)
                .orElseThrow(() -> new NotFoundAlarmPreferenceRow(AlarmPreferenceCoreErrorCode.NOT_FOUND_ALARM_PREFERENCE_ROW));
        pref.toggle();
    }

}