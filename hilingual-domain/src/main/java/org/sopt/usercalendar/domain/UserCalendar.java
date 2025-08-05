package org.sopt.usercalendar.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    @Column(name = UserCalendarTableConstants.COLUMN_IS_WRITTEN, nullable = false)
    private Boolean isWritten;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = UserCalendarTableConstants.COLUMN_USER_ID, nullable = false)
    private User user;

    public void markWritten(){
        this.isWritten = true;
    }

    public static UserCalendar create(LocalDate date, Boolean isWritten, User user) {
        return new UserCalendar(null, date, isWritten, user); // id=null
    }
}
