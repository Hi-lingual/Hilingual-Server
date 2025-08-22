package org.sopt.usercalendar.repository;

import org.sopt.user.domain.User;
import org.sopt.usercalendar.domain.UserCalendar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {

    /**
     * TODO : 추후 위에 두 User로 탐색하는 메서드 없애고, 아래 ID로 조회하는 메서드 사용하는 걸로 리팩하기!
     */
    boolean existsByUserAndDate(User user, LocalDate date);
    Optional<UserCalendar> findByUserAndDate(User user, LocalDate date);

//    boolean existsByUserIdAndDate(Long userId, LocalDate date);
//    Optional<UserCalendar> findByUserIdAndDate(Long userId, LocalDate date);

    @Query("""
        SELECT uc.date FROM UserCalendar uc
        WHERE uc.user.id = :userId
          AND uc.status  = org.sopt.usercalendar.domain.WriteStatus.WRITTEN
          AND YEAR(uc.date) = :year
          AND MONTH(uc.date) = :month
    """)
    List<LocalDate> findWrittenDatesByUserIdAndYearAndMonth(
            @Param("userId") Long userId,
            @Param("year")   int  year,
            @Param("month")  int  month
    );
}
