package org.sopt.recoveryticket.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recoveryticket.domain.RecoveryTicket;
import org.sopt.recoveryticket.exception.RecoveryTicketCoreErrorCode;
import org.sopt.recoveryticket.exception.RecoveryTicketNotFoundException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class RecoveryTicketFacade {
    private final RecoveryTicketRetriever recoveryTicketRetriever;
    private final RecoveryTicketSaver recoveryTicketSaver;

    public RecoveryTicket getValidTicket(final Long userId, final LocalDate writtenDate) {
        return recoveryTicketRetriever.findValidTicket(userId, writtenDate)
                .orElseThrow(() -> new RecoveryTicketNotFoundException(RecoveryTicketCoreErrorCode.RECOVERY_TICKET_NOT_FOUND));
    }

    public boolean existsValidTicket(final Long userId, final LocalDate targetDate) {
        return recoveryTicketRetriever.existsValidTicket(userId, targetDate);
    }

    public RecoveryTicket save(final RecoveryTicket ticket) {
        return recoveryTicketSaver.save(ticket);
    }
}
