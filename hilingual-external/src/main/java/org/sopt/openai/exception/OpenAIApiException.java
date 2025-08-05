package org.sopt.openai.exception;

import org.sopt.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

public class OpenAIApiException extends OpenAIBaseException {
    protected OpenAIApiException(ErrorCode errorCode) {
        super(errorCode);
    }

    @Override
    public HttpStatus getStatus() {
        return HttpStatus.SERVICE_UNAVAILABLE;
    }

}
