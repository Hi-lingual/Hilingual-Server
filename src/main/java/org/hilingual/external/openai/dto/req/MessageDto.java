package org.hilingual.external.openai.dto.req;

import static org.hilingual.external.openai.OpenAIConstant.SYSTEM;
import static org.hilingual.external.openai.OpenAIConstant.USER;

public record MessageDto(String role, String content) {
    public static MessageDto system(String content) {
        return new MessageDto(SYSTEM, content);
    }

    public static MessageDto user(String content) {
        return new MessageDto(USER, content);
    }
}