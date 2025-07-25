package org.hilingual.external.openai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public record GptRequest(
        String model,
        List<Map<String, String>> messages,
        double temperature,
        @JsonProperty("max_tokens") int maxTokens
) {
    public static GptRequest of(String prompt, String originalText) {
        return new GptRequest(
                "gpt-4o",
                List.of(
                        Map.of("role", "system", "content", prompt),
                        Map.of("role", "user", "content", originalText)
                ),
                0.3,
                1200
        );
    }
}