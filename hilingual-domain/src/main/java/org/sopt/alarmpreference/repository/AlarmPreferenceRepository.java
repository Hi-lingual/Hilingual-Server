package org.sopt.alarmpreference.repository;

import org.sopt.alarmpreference.domain.AlarmPreference;
import org.sopt.alarmpreference.type.AlarmType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AlarmPreferenceRepository extends JpaRepository<AlarmPreference, Long> {

    // 특정 유저의 모든 알림설정 조회
    List<AlarmPreference> findByUserId(Long userId);

    // 특정 유저의 특정 알림 타입 설정을 조회
    Optional<AlarmPreference> findByUserIdAndAlarmType(Long userId, AlarmType alarmType);


    // 특정 알림 타입이 '활성화'된 유저 id만 조회
    @Query("""
           select ap.user.id
             from AlarmPreference ap
            where ap.alarmType = :alarmType
              and ap.isEnabled = true
           """)
    List<Long> findEnabledUserIdsByType(AlarmType alarmType);

    @Modifying(clearAutomatically = true)
    @Query("delete from AlarmPreference a where a.user.id = :userId")
    void deleteAllByUserId(Long userId);

}