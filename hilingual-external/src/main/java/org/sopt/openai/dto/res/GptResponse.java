package org.sopt.openai.dto.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GptResponse(
        List<Choice> choices
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(
            Message message
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Message(
            String role,
            String content
    ) {}
}