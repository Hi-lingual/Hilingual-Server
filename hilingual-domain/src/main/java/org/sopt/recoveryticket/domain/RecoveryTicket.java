package org.sopt.recoveryticket.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.sopt.common.config.BaseTimeEntity;
import org.sopt.user.domain.User;

import java.time.LocalDate;

import static org.sopt.recoveryticket.domain.RecoveryTicketTableConstants.*;
import static org.sopt.recoveryticket.domain.RecoveryTicketTableConstants.UK_RECOVERY_TICKET_USER_WRITTEN_DATE;

@Entity
@Table(
        name = RecoveryTicketTableConstants.TABLE_RECOVERY_TICKET,
        uniqueConstraints = {
                @UniqueConstraint(
                        name = UK_RECOVERY_TICKET_USER_WRITTEN_DATE,
                        columnNames = {COLUMN_USER_ID, COLUMN_WRITTEN_DATE}
                )
        }
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RecoveryTicket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COLUMN_ID)
    private Long id;

    @Column(name = COLUMN_WRITTEN_DATE, nullable = false)
    private LocalDate writtenDate;

    @Column(name = COLUMN_IS_USED, nullable = false)
    private Boolean isUsed = Boolean.FALSE; // 광고 시청 O, 일기 작성 X인 상태
    // isUsed = true인 경우 광고 시청 O, 일기 작성 O인 상태

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public void markIsUsed() {
        this.isUsed = true;
    }
}
