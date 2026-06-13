package org.sopt.recoveryticket.repository;

import org.sopt.recoveryticket.domain.RecoveryTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


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
}

