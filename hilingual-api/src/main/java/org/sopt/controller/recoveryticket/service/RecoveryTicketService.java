package org.sopt.controller.recoveryticket.service;

import lombok.RequiredArgsConstructor;
import org.sopt.controller.recoveryticket.dto.RecoveryTicketUsedRes;
import org.sopt.controller.recoveryticket.exception.NoRecoveryChancesLeftException;
import org.sopt.controller.recoveryticket.exception.RecoveryTicketAlreadyIssuedException;
import org.sopt.controller.recoveryticket.exception.RecoveryTicketApiErrorCode;
import org.sopt.controller.recoveryticket.exception.InvalidRecoveryTargetDateException;
import org.sopt.diary.facade.DiaryFacade;
import org.sopt.recoveryticket.domain.RecoveryTicket;
import org.sopt.recoveryticket.facade.RecoveryTicketFacade;
import org.sopt.user.domain.User;
import org.sopt.user.facade.UserFacade;
import org.sopt.userprofile.domain.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecoveryTicketService {
    private final UserFacade userFacade;
    private final DiaryFacade diaryFacade;
    private final RecoveryTicketFacade recoveryTicketFacade;

    @Transactional
    public RecoveryTicketUsedRes issueRecoveryTicket(
            final Long userId,
            final String userTargetDate,
            final ZoneId userZone
    ) {
        LocalDate targetDate = LocalDate.parse(userTargetDate);
        LocalDate today = LocalDate.now(userZone);
        LocalDate yesterday = today.minusDays(1);

        // 미래이거나 오늘 날짜는 복구 불가 (과거 날짜만 가능)
        if (!targetDate.isBefore(yesterday)) {
            throw new InvalidRecoveryTargetDateException(RecoveryTicketApiErrorCode.INVALID_TARGET_DATE);
        }

        User user = userFacade.getUserById(userId);
        UserProfile profile = user.getUserProfile();

        // 남은 부활권 기회가 없는 경우
        if (profile.getRecoveryChanceCount() <= 0) {
            throw new NoRecoveryChancesLeftException(RecoveryTicketApiErrorCode.NO_RECOVERY_CHANCES_LEFT);
        }

        // 해당 날짜에 이미 작성 완료된 일기가 있는 경우 검증
        diaryFacade.validateNotExists(user, targetDate);

        // 해당 날짜에 이미 발급받은 미사용 티켓이 있는 경우
        if (recoveryTicketFacade.existsValidTicket(userId, targetDate)) {
            throw new RecoveryTicketAlreadyIssuedException(RecoveryTicketApiErrorCode.TICKET_ALREADY_ISSUED);
        }

        // 유저 부활권 기회 1 차감
        profile.decreaseRecoveryChanceCount();

        RecoveryTicket ticket = RecoveryTicket.create(user, targetDate);
        recoveryTicketFacade.save(ticket);

        return new RecoveryTicketUsedRes(
                ticket.getId(),
                targetDate.toString(),
                Long.valueOf(profile.getRecoveryChanceCount()) // Integer to Long 형변환
        );
    }

}
