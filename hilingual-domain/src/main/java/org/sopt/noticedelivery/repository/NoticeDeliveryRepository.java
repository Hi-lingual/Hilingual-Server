package org.sopt.noticedelivery.repository;

import org.sopt.noticedelivery.domain.NoticeDelivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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
}