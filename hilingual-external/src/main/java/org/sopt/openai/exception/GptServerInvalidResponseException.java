package org.sopt.openai.exception;


import org.sopt.exception.code.ErrorCode;

public class GptServerInvalidResponseException extends OpenAIApiException {
    public GptServerInvalidResponseException(ErrorCode errorCode) {
        super(errorCode);
    }
}
