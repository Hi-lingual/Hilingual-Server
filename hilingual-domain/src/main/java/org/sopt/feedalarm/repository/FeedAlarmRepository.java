package org.sopt.feedalarm.repository;

import org.sopt.feedalarm.domain.FeedAlarm;
import org.sopt.feedalarm.type.FeedAlarmType;
import org.sopt.feedalarm.type.TargetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    boolean existsByUserIdAndActorIdAndTypeAndTargetId(
            Long userId, Long actorId, FeedAlarmType type, Long targetId
    );

    boolean existsByUserIdAndActorIdAndTypeAndTargetType(
            Long userId, Long actorId, FeedAlarmType type, TargetType targetType
    );

}