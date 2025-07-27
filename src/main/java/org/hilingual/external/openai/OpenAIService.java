package org.hilingual.external.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hilingual.external.openai.client.OpenAIClient;
import org.hilingual.external.openai.dto.GptRequest;
import org.hilingual.external.openai.dto.GptResponse;
import org.hilingual.external.openai.exception.GptResponseParsingException;
import org.hilingual.external.openai.exception.GptServerEmptyContentException;
import org.hilingual.external.openai.exception.GptServerInvalidResponseException;
import org.hilingual.external.openai.exception.OpenAIErrorCode;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OpenAIService {

    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper;

    public Map<String, Object> getDiaryFeedback(String prompt, String originalText) {
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
            contentJson = contentJson.replace("```json", "")
                    .replace("```", "")
                    .trim();
        }

        try {
            return objectMapper.readValue(contentJson, Map.class);
        } catch (Exception e) {
            log.warn("서버에서 GPT 응답 파싱에 실패했습니다 : {}", contentJson);
            throw new GptResponseParsingException(OpenAIErrorCode.GPT_RESPONSE_PARSING_ERROR);
        }
    }
}