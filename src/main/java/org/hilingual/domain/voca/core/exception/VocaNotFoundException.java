package org.hilingual.domain.voca.core.exception;

import org.hilingual.domain.voca.core.exception.VocaCoreErrorCode;
import org.springframework.http.HttpStatus;

// TODO : User 도메인 구현되면 구현 예정
public class VocaNotFoundException extends VocaCoreException {
    public VocaNotFoundException() {
        super(VocaCoreErrorCode.VOCA_NOT_FOUND);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.NOT_FOUND;
    }
}
