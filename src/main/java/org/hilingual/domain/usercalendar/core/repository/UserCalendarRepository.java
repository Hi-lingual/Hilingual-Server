package org.hilingual.domain.usercalendar.core.repository;

import org.hilingual.domain.user.core.domain.User;
import org.hilingual.domain.usercalendar.core.domain.UserCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {

    boolean existsByUserAndDate(User user, LocalDate date);

    Optional<UserCalendar> findByUserAndDate(User user, LocalDate date);

    @Query("""
        SELECT uc.date FROM UserCalendar uc
        WHERE uc.user.id = :userId
          AND uc.isWritten = true
          AND YEAR(uc.date) = :year
          AND MONTH(uc.date) = :month
    """)
    List<LocalDate> findWrittenDatesByUserIdAndYearAndMonth(
            @Param("userId") Long userId,
            @Param("year")   int  year,
            @Param("month")  int  month
    );
}
