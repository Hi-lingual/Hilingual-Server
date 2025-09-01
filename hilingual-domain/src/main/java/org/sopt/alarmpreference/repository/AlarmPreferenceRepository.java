package org.sopt.alarmpreference.repository;

import org.sopt.alarmpreference.domain.AlarmPreference;
import org.sopt.alarmpreference.type.AlarmType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlarmPreferenceRepository extends JpaRepository<AlarmPreference, Long> {

    // 특정 유저의 모든 알림설정 조회
    List<AlarmPreference> findByUserId(Long userId);

    // 특정 유저의 특정 알림 타입 설정을 조회
    Optional<AlarmPreference> findByUserIdAndAlarmType(Long userId, AlarmType alarmType);

}