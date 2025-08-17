package org.sopt.jwt.support.logsupport;

import lombok.extern.slf4j.Slf4j;
import org.sopt.exception.code.ErrorCode;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ErrorLogger {
    public void log(ErrorCode errorCode, Exception e) {
        if (errorCode.getHttpStatus().is4xxClientError()) {
            log.warn("Client error [{}]: {}", errorCode.getCode(), e.getMessage());
        } else if (errorCode.getHttpStatus().is5xxServerError()) {
            log.error("Server error [{}]: {}", errorCode.getCode(), e.getMessage(), e);
        }
    }
}