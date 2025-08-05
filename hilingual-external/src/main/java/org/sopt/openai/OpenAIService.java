package org.sopt.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sopt.openai.client.OpenAIClient;
import org.sopt.openai.dto.req.GptRequest;
import org.sopt.openai.dto.res.GptFeedbackResponse;
import org.sopt.openai.dto.res.GptResponse;
import org.sopt.openai.exception.GptResponseParsingException;
import org.sopt.openai.exception.GptServerEmptyContentException;
import org.sopt.openai.exception.GptServerInvalidResponseException;
import org.sopt.openai.exception.OpenAIErrorCode;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper;

    public GptFeedbackResponse getDiaryFeedback(String prompt, String originalText) {
        GptRequest request = GptRequest.of(prompt, originalText);
        GptResponse response = openAIClient.callChatCompletion(request);

        if (response.choices() == null || response.choices().isEmpty()) {
            throw new GptServerInvalidResponseException(OpenAIErrorCode.GPT_SERVER_INVALID_RESPONSE);
        }

        String contentJson = response.choices().get(0).message().content();
        if (contentJson == null) {
            throw new GptServerEmptyContentException(OpenAIErrorCode.GPT_SERVER_EMPTY_CONTENT);
        }

        if (contentJson.startsWith("```")) {
            contentJson = contentJson
                    .replaceAll("(?i)```json", "")
                    .replaceAll("(?i)```", "")
                    .trim();
        }

        try {
            return objectMapper.readValue(contentJson, GptFeedbackResponse.class);
        } catch (Exception e) {
            log.warn("서버에서 GPT 응답 파싱에 실패했습니다. contentJson={}, error={}", contentJson, e.getMessage(), e);
            throw new GptResponseParsingException(OpenAIErrorCode.GPT_RESPONSE_PARSING_ERROR);
        }
    }
}