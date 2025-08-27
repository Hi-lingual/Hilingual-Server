package org.sopt.alarmpreference.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.domain.AlarmPreference;
import org.sopt.alarmpreference.exception.AlarmPreferenceCoreErrorCode;
import org.sopt.alarmpreference.exception.NotFoundAlarmPreferenceRow;
import org.sopt.alarmpreference.repository.AlarmPreferenceRepository;
import org.sopt.alarmpreference.type.AlarmType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AlarmPreferenceRetriever {

    private final AlarmPreferenceRepository alarmPreferenceRepository;

    public EnumMap<AlarmType, Boolean> getAlarmStatusMap(Long userId) {
        List<AlarmPreference> prefs = alarmPreferenceRepository.findByUserId(userId);

        if (prefs.isEmpty()) {
            throw new NotFoundAlarmPreferenceRow(AlarmPreferenceCoreErrorCode.NOT_FOUND_ALARM_PREFERENCE_ROW);
        }

        EnumMap<AlarmType, Boolean> map = new EnumMap<>(AlarmType.class);
        prefs.forEach(p -> map.put(p.getAlarmType(), Boolean.TRUE.equals(p.getIsEnabled())));
        return map;
    }
}