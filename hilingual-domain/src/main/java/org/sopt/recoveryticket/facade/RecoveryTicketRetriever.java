package org.sopt.recoveryticket.facade;

import lombok.RequiredArgsConstructor;
import org.sopt.recoveryticket.domain.RecoveryTicket;
import org.sopt.recoveryticket.repository.RecoveryTicketRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class RecoveryTicketRetriever {

    private final RecoveryTicketRepository recoveryTicketRepository;

    public List<RecoveryTicket> findUnlockedTicketsByMonth(final Long userId, final int year, final int month) {
        // 년/월 정보를 바탕으로 해당 월의 1일과 말일을 계산
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return recoveryTicketRepository.findUnlockedTicketsByUserIdAndDateRange(userId, startDate, endDate);
    }

    public Optional<RecoveryTicket> findValidTicket(final Long userId, final LocalDate writtenDate) {
        return recoveryTicketRepository.findByUserIdAndWrittenDateAndIsUsedFalse(userId, writtenDate);
    }

    public boolean existsValidTicket(final Long userId, final LocalDate writtenDate) {
        return recoveryTicketRepository.existsByUserIdAndWrittenDateAndIsUsedFalse(userId, writtenDate);
    }

}