package org.sopt.noticedelivery.repository;

import org.sopt.noticedelivery.domain.NoticeDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeDeliveryRepository extends JpaRepository<NoticeDelivery, Long> {

    // userId에 해당하는 공지문 상세 조회
    @Query("""
        select nd
          from NoticeDelivery nd
          join fetch nd.notice n
          join fetch n.noticeDetail d
         where nd.user.id = :userId
           and n.id = :noticeId
    """)
    Optional<NoticeDelivery> findByUserIdAndNoticeIdWithDetail(@Param("userId") Long userId,
                                                               @Param("noticeId") Long noticeId);

    // 최근 알림 목록 최대 500개, 수신시각 최신순
    @Query("""
        select nd
          from NoticeDelivery nd
          join fetch nd.notice n
         where nd.user.id = :userId
         order by nd.deliveredAt desc, nd.id desc
    """)
    List<NoticeDelivery> findTop500ByUserIdOrderByDeliveredAtDesc(Long userId);

    @Modifying
    @Query(value = """
        with ranked as (
            select id,
                   row_number() over (partition by user_id order by delivered_at desc) as rn
            from notice_delivery
        )
        delete from notice_delivery nd
        using ranked r
        where nd.id = r.id
          and r.rn > :limit
        """, nativeQuery = true)
    void deleteAllUsersBeyondLimit(@Param("limit") int limit);
}