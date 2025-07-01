package org.hilingual.domain.usercalendar;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hilingual.common.domain.BaseTimeEntity;
import org.hilingual.domain.user.User;

import java.time.LocalDate;

import static org.hilingual.domain.usercalendar.UserCalendarTableConstants.*;

@Entity
@Table(name = TABLE_USER_CALENDAR)
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
}