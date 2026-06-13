package org.sopt.controller.recoveryticket.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum RecoveryTicketApiErrorCode implements ErrorCode {
    // 400
    INVALID_TARGET_DATE(HttpStatus.BAD_REQUEST, 40051, "일반 작성 가능한 날짜는 복구 불가합니다."),

    // 403
    NO_RECOVERY_CHANCES_LEFT(HttpStatus.FORBIDDEN, 40306, "남은 기록 살리기 기회가 없습니다."),

    // 409
    TICKET_ALREADY_ISSUED(HttpStatus.CONFLICT, 40906, "해당 날짜에 이미 발급받은 미사용 티켓이 있습니다.");

    public final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    @Override
    public int getCode(){
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}