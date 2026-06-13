package org.sopt.recoveryticket.exception;

import lombok.RequiredArgsConstructor;
import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum RecoveryTicketCoreErrorCode implements ErrorCode {

    // 400
    RECOVERY_TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, 40050, "해당 날짜에 유효한 스트릭 부활권이 존재하지 않거나 이미 사용되었습니다."),
    ;

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