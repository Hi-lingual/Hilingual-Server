package org.sopt.feedalarm.repository;

import org.sopt.feedalarm.domain.FeedAlarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FeedAlarmRepository extends JpaRepository<FeedAlarm, Long> {

    Optional<FeedAlarm> findByIdAndUserId(Long alarmId, Long userId);

}