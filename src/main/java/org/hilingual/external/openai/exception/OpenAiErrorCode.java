package org.hilingual.external.openai.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.hilingual.common.exception.code.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum OpenAiErrorCode implements ErrorCode {

    // 500
    GPT_RESPONSE_PARSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 50002, "서버에서 GPT 응답 파싱에 실패했습니다."),

    // 503
    GPT_SERVER_INVALID_RESPONSE(HttpStatus.SERVICE_UNAVAILABLE,50302,"GPT 서버 응답이 비어있거나 잘못되었습니다."),
    GPT_SERVER_EMPTY_CONTENT(HttpStatus.SERVICE_UNAVAILABLE, 50303,"GPT 응답에서 content가 비어있습니다."),

    ;

    public final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus(){
        return httpStatus;
    }

    @Override
    public int getCode(){
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
