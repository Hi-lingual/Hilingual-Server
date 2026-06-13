package org.sopt.controller.recoveryticket.api;

import lombok.RequiredArgsConstructor;
import org.sopt.annotation.UserTimezone;
import org.sopt.controller.recoveryticket.dto.RecoveryTicketTargetReq;
import org.sopt.controller.recoveryticket.dto.RecoveryTicketUsedRes;
import org.sopt.controller.recoveryticket.service.RecoveryTicketService;
import org.sopt.jwt.annotation.UserId;
import org.sopt.web.UserZone;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/me/recovery-ticket")
@RequiredArgsConstructor
public class RecoveryTicketController {

    private final RecoveryTicketService recoveryTicketService;

    // 보상형 광고 시청 완료 후 티켓 발급 API
    @PostMapping
    public ResponseEntity<RecoveryTicketUsedRes> issueRecoveryTicket(
            @UserId Long userId,
            @UserTimezone UserZone userZone,
            @RequestBody RecoveryTicketTargetReq req
    ) {
        return ResponseEntity.ok(recoveryTicketService.issueRecoveryTicket(userId, req.targetDate(), userZone.zoneId()));
    }
}
