package org.sopt.noticedelivery.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.notice.domain.Notice;
import org.sopt.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = NoticeDeliveryTableConstants.TABLE_NOTICE_DELIVERY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = NoticeDeliveryTableConstants.COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = NoticeDeliveryTableConstants.COLUMN_NOTICE_ID, nullable = false)
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = NoticeDeliveryTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    @Column(name = NoticeDeliveryTableConstants.COLUMN_DELIVERED_AT, nullable = false)
    private LocalDateTime deliveredAt;

    @Column(name = NoticeDeliveryTableConstants.COLUMN_READ_AT)
    private LocalDateTime readAt;

}
