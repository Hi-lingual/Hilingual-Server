package org.sopt.recoveryticket.repository;

import org.sopt.recoveryticket.domain.RecoveryTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface RecoveryTicketRepository extends JpaRepository<RecoveryTicket, Long> {

    @Query("""
        SELECT rt FROM RecoveryTicket rt
        WHERE rt.user.id = :userId
          AND rt.isUsed = false
          AND rt.writtenDate BETWEEN :startDate AND :endDate
    """)
    List<RecoveryTicket> findUnlockedTicketsByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // 특정 날짜의 미사용 티켓 존재하는지 조회
    @Query("""
        SELECT rt FROM RecoveryTicket rt
        WHERE rt.user.id = :userId
          AND rt.writtenDate = :writtenDate
          AND rt.isUsed = false
    """)
    Optional<RecoveryTicket> findByUserIdAndWrittenDateAndIsUsedFalse(
            @Param("userId") Long userId,
            @Param("writtenDate") LocalDate writtenDate
    );
}

