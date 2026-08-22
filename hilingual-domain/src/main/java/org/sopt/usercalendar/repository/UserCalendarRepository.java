package org.sopt.usercalendar.repository;

import org.sopt.user.domain.User;
import org.sopt.usercalendar.domain.UserCalendar;
import org.sopt.usercalendar.domain.WriteStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserCalendarRepository extends JpaRepository<UserCalendar, Long> {

    /**
     * TODO : 추후 위에 두 User로 탐색하는 메서드 없애고, 아래 ID로 조회하는 메서드 사용하는 걸로 리팩하기!
     */
    @Query("SELECT CASE WHEN EXISTS (SELECT 1 FROM UserCalendar uc WHERE uc.user = :user AND uc.date = :date AND uc.status != 'DELETED') THEN true ELSE false END")
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

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        update UserCalendar uc
           set uc.status = :status
         where uc.user.id = :userId
           and uc.date = :date
    """)
    int updateStatusByUserAndDate(@Param("userId") Long userId,
                                  @Param("date") LocalDate date,
                                  @Param("status") WriteStatus status);

    void deleteAllByUserId(Long userId);

    Optional<UserCalendar> findByUserIdAndDate(Long userId, LocalDate date);


    @Query("""
    SELECT COUNT(uc) FROM UserCalendar uc
    WHERE uc.user.id = :userId
      AND uc.status IN (
      org.sopt.usercalendar.domain.WriteStatus.WRITTEN,
      org.sopt.usercalendar.domain.WriteStatus.RECOVERED
      )
    """)
    long countWrittenByUserId(@Param("userId") Long userId);

    @Query("""
    SELECT uc FROM UserCalendar uc
    WHERE uc.user.id = :userId
        AND uc.date BETWEEN :start AND :end
    """)
    List<UserCalendar> findAllByUserIdAndDateBetween(@Param("userId") Long usrId,
                                                     @Param("start") LocalDate start,
                                                     @Param("end") LocalDate end);
}
