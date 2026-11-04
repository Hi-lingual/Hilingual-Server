package org.sopt.openai.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.sopt.openai.OpenAIConstant;

import java.util.List;

public record GptRequest(
        String model,
        List<MessageDto> messages,
        double temperature,
        @JsonProperty("max_completion_tokens") int maxTokens
) {
    public static GptRequest of(String prompt, String originalText) {
        return new GptRequest(
                OpenAIConstant.MODEL,
                List.of(
                        MessageDto.system(prompt),
                        MessageDto.user(originalText)
                ),
                OpenAIConstant.TEMPERATURE,
                OpenAIConstant.MAX_TOKENS
        );
    }
}