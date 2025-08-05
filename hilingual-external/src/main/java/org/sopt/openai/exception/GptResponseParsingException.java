package org.sopt.openai.exception;


import org.sopt.exception.code.ErrorCode;

public class GptResponseParsingException extends OpenAIApiException {
    public GptResponseParsingException(ErrorCode errorCode) {
        super(errorCode);
    }
}
