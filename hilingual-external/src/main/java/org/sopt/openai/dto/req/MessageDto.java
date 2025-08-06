package org.sopt.openai.dto.req;

import org.sopt.openai.OpenAIConstant;

public record MessageDto(String role, String content) {
    public static MessageDto system(String content) {
        return new MessageDto(OpenAIConstant.SYSTEM, content);
    }

    public static MessageDto user(String content) {
        return new MessageDto(OpenAIConstant.USER, content);
    }
}