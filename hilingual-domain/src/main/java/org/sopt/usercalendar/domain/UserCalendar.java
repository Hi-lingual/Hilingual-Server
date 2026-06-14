package org.sopt.usercalendar.domain;

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

@Entity
@Table(
        name = UserCalendarTableConstants.TABLE_USER_CALENDAR,
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCalendar extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = UserCalendarTableConstants.COLUMN_DATE, nullable = false)
    private LocalDate date;
  
    @Enumerated(EnumType.STRING)
    @Column(name = UserCalendarTableConstants.COLUMN_STATUS, nullable = false, length = 16)
    private WriteStatus status = WriteStatus.NONE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = UserCalendarTableConstants.COLUMN_USER_ID, nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public void updateStatus(WriteStatus status) {
        this.status = status;
    }

    public static UserCalendar create(LocalDate date, WriteStatus status, User user) {
        return new UserCalendar(null, date, status, user);
    }
}
