package org.hilingual.external.openai.exception;

import org.hilingual.common.exception.code.ErrorCode;

public class GptResponseParsingException extends OpenAIApiException {
    public GptResponseParsingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
