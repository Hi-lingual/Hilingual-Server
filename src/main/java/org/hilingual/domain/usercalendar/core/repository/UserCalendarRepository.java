package org.hilingual.domain.usercalendar.core.repository;

import org.hilingual.domain.usercalendar.core.domain.UserCalendar;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface UserCalendarRepository extends Repository<UserCalendar, LocalDate> {

    @Query("""
        SELECT uc.date FROM UserCalendar uc
        WHERE uc.user.id = :userId
          AND uc.isWritten = true
          AND YEAR(uc.date) = :year
          AND MONTH(uc.date) = :month
    """)
    List<LocalDate> findWrittenDatesByUserIdAndYearAndMonth(
            @Param("userId") Long userId,
            @Param("year") int year,
            @Param("month") int month
    );
}
