package org.sopt.alarmpreference.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.alarmpreference.domain.AlarmPreference;
import org.sopt.alarmpreference.type.AlarmType;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class AlarmPreferenceFacade {

    private final AlarmPreferenceRetriever alarmPreferenceRetriever;
    private final AlarmPreferenceUpdater alarmPreferenceUpdater;
    private final AlarmPreferenceSaver alarmPreferenceSaver;
    private final AlarmPreferenceRemover alarmPreferenceRemover;

    public Map<AlarmType, Boolean> getAlarmStatusMap(final long userId) {
        return alarmPreferenceRetriever.getAlarmStatusMap(userId);
    }

    @Transactional
    public void toggle(final long userId, final AlarmType type) {
        alarmPreferenceUpdater.toggle(userId, type);
    }

    @Transactional
    public AlarmPreference save(AlarmPreference alarmPreference) {
        return alarmPreferenceSaver.save(alarmPreference);
    }

    // type 에 해당하는 알람 수신 여부
    @Transactional(readOnly = true)
    public boolean isEnabled(final long userId, final AlarmType type) {
        return alarmPreferenceRetriever.isEnabled(userId, type);
    }

    public void deleteAllByUserId(final long userId) {
        alarmPreferenceRemover.deleteAllByUserId(userId);
    }
}