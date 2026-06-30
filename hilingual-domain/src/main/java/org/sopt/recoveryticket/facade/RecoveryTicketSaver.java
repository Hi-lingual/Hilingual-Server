package org.sopt.recoveryticket.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recoveryticket.domain.RecoveryTicket;
import org.sopt.recoveryticket.repository.RecoveryTicketRepository;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class RecoveryTicketSaver {

    private final RecoveryTicketRepository recoveryTicketRepository;

    public RecoveryTicket save(RecoveryTicket recoveryTicket) {
        return recoveryTicketRepository.save(recoveryTicket);
    }
}