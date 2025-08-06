package org.sopt.exception.code;

import org.springframework.http.HttpStatus;

public interface ApiCode {
    HttpStatus getHttpStatus();
    int getCode();
    String getMessage();
}