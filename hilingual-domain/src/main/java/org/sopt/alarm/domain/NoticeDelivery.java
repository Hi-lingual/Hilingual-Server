package org.sopt.alarm.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.sopt.user.domain.User;

import java.time.LocalDateTime;

@Entity
@Table(name = AlarmTableConstants.TABLE_NOTICE_DELIVERY)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = AlarmTableConstants.COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = AlarmTableConstants.COLUMN_NOTICE_ID, nullable = false)
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = AlarmTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    @Column(name = AlarmTableConstants.COLUMN_DELIVERED_AT, nullable = false)
    private LocalDateTime deliveredAt;

    @Column(name = AlarmTableConstants.COLUMN_READ_AT)
    private LocalDateTime readAt;

}
