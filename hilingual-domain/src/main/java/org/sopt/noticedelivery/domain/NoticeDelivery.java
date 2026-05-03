package org.sopt.noticedelivery.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.sopt.notice.domain.Notice;
import org.sopt.user.domain.User;

import java.time.LocalDateTime;
import static org.sopt.noticedelivery.domain.NoticeDeliveryTableConstants.*;

@Entity
@Table(
        name = NoticeDeliveryTableConstants.TABLE_NOTICE_DELIVERY,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = UK_NOTICE_DELIVERY_NOTICE_USER,
                        columnNames = {
                                COLUMN_NOTICE_ID,
                                COLUMN_USER_ID
                        }
                )
        }
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NoticeDelivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_NOTICE_ID, nullable = false)
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Column(name = COLUMN_DELIVERED_AT, nullable = false)
    private LocalDateTime deliveredAt;

    @Column(name = COLUMN_READ_AT)
    private LocalDateTime readAt;

    public void markReadIfNeeded(LocalDateTime now) {
        if (this.readAt == null) {
            this.readAt = now;
        }
    }

    public static NoticeDelivery create(Notice notice, User user, LocalDateTime deliveredAt) {
        return new NoticeDelivery(null, notice, user, deliveredAt, null);
    }

}
