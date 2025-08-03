package org.hilingual.external.openai.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static org.hilingual.external.openai.OpenAIConstant.*;

public record GptRequest(
        String model,
        List<MessageDto> messages,
        double temperature,
        @JsonProperty("max_tokens") int maxTokens
) {
    public static GptRequest of(String prompt, String originalText) {
        return new GptRequest(
                MODEL,
                List.of(
                        MessageDto.system(prompt),
                        MessageDto.user(originalText)
                ),
                TEMPERATURE,
                MAX_TOKENS
        );
    }
}