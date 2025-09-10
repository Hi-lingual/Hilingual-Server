package org.sopt.feedalarm.repository;

import org.sopt.feedalarm.domain.FeedAlarm;
import org.sopt.feedalarm.type.FeedAlarmType;
import org.sopt.feedalarm.type.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FeedAlarmRepository extends JpaRepository<FeedAlarm, Long> {

    Optional<FeedAlarm> findByIdAndUserId(Long alarmId, Long userId);

    @Query("""
        select f
          from FeedAlarm f
         where f.user.id = :userId
         order by f.createdAt desc, f.id desc
    """)
    List<FeedAlarm> findTop500ByUserIdOrderByCreatedAtDesc(Long userId);

    @Modifying
    @Query(value = """
        with ranked as (
            select id,
                   row_number() over (partition by user_id order by created_at desc) as rn
            from feed_alarm
        )
        delete from feed_alarm fa
        using ranked r
        where fa.id = r.id
          and r.rn > :limit
        """, nativeQuery = true)
    void deleteAllUsersBeyondLimit(@Param("limit") int limit);

    // 최근 1분 내 동일 알림 존재 여부 확인
    @Query("""
    select (count(a) > 0)
    from FeedAlarm a
    where a.user.id   = :userId
      and a.actorId  = :actorId
      and a.type     = :type
      and a.targetId = :targetId
      and a.createdAt >= :threshold
""")
    boolean existsRecentSameAlarm(
            @Param("userId") Long userId,
            @Param("actorId") Long actorId,
            @Param("type") FeedAlarmType type,
            @Param("targetId") Long targetId,
            @Param("threshold") LocalDateTime threshold
    );

    void deleteAllByUserId(Long userId);

}