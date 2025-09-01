package org.sopt.alarmpreference.repository;

import org.sopt.alarmpreference.domain.AlarmPreference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmPreferenceRepository extends JpaRepository<AlarmPreference, Long> {
}
