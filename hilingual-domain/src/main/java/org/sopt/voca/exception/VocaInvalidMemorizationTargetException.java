package org.sopt.voca.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class VocaInvalidMemorizationTargetException extends VocaCoreException {

    public VocaInvalidMemorizationTargetException(ErrorCode errorCode){
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus(){
        return HttpStatus.BAD_REQUEST;
    }


}