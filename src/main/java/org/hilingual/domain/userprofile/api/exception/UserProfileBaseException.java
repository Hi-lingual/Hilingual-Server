package org.hilingual.domain.userprofile.api.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.base.HilingualBaseException;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class UserProfileBaseException extends HilingualBaseException {
    private final ErrorCode errorCode;
    public abstract HttpStatus getStatus();
}
