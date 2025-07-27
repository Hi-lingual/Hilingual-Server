package org.hilingual.external.openai.exception;

import org.hilingual.common.exception.code.ErrorCode;

public class GptServerEmptyContentException extends OpenAIApiException {
    public GptServerEmptyContentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
