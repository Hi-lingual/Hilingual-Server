package org.hilingual.domain.usercalendar.core.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hilingual.common.domain.BaseTimeEntity;
import org.hilingual.domain.user.core.domain.User;

import java.time.LocalDate;

import static org.hilingual.domain.usercalendar.core.domain.UserCalendarTableConstants.*;

@Entity
@Table(
        name = TABLE_USER_CALENDAR,
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date"})
)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserCalendar extends BaseTimeEntity {

    @Id
    @Column(name = COLUMN_DATE)
    private LocalDate date;

    @Column(name = COLUMN_IS_WRITTEN, nullable = false)
    private Boolean isWritten;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = COLUMN_USER_ID, nullable = false)
    private User user;

    public void markWritten(){
        this.isWritten = true;
    }

    public static UserCalendar create(LocalDate date, Boolean isWritten, User user) {
        return new UserCalendar(date, isWritten, user);
    }
}
