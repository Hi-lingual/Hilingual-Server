package org.hilingual.external.openai.exception;

import org.hilingual.common.exception.code.ErrorCode;

public class GptServerInvalidResponseException extends OpenAIApiException {
    public GptServerInvalidResponseException(ErrorCode errorCode) {
        super(errorCode);
    }
}
