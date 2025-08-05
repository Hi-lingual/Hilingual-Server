package org.sopt.openai.exception;


import org.sopt.exception.code.ErrorCode;

public class GptServerEmptyContentException extends OpenAIApiException {
    public GptServerEmptyContentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
