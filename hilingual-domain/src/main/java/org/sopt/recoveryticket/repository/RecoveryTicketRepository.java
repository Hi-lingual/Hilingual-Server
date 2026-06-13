package org.sopt.recoveryticket.repository;

import org.sopt.recoveryticket.domain.RecoveryTicket;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RecoveryTicketRepository extends JpaRepository<RecoveryTicket, Long> {

}

