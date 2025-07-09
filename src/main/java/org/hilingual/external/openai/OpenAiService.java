package org.hilingual.external.openai;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.hilingual.external.openai.exception.GptResponseParsingException;
import org.hilingual.external.openai.exception.GptServerEmptyContentException;
import org.hilingual.external.openai.exception.GptServerInvalidResponseException;
import org.hilingual.external.openai.exception.OpenAiErrorCode;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class OpenAiService {

    private final RestTemplate restTemplate;
    private final HttpHeaders httpHeaders;
    private final ObjectMapper objectMapper;

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";

    public Map<String, Object> getDiaryFeedback(String prompt, String originalText) {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-4o");
        requestBody.put("messages", List.of(
                Map.of("role", "system", "content", prompt),
                Map.of("role", "user", "content", originalText)
        ));
        requestBody.put("temperature", 0.3);
        requestBody.put("max_tokens", 1200);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, httpHeaders);

        ResponseEntity<Map> response = restTemplate.exchange(
                OPENAI_URL,
                HttpMethod.POST,
                entity,
                Map.class
        );

        Map<String, Object> body = response.getBody();
        if (body == null || !body.containsKey("choices")) {
            throw new GptServerInvalidResponseException(OpenAiErrorCode.GPT_SERVER_INVALID_RESPONSE);
        }

        Map<String, Object> message = (Map<String, Object>) ((List<Map<String, Object>>) body.get("choices")).get(0).get("message");
        String contentJson = (String) message.get("content");

        if (contentJson == null) {
            throw new GptServerEmptyContentException(OpenAiErrorCode.GPT_SERVER_EMPTY_CONTENT);
        }

        if (contentJson.startsWith("```")) {
            contentJson = contentJson.replace("```json", "")
                    .replace("```", "")
                    .trim();
        }

        try {
            return objectMapper.readValue(contentJson, Map.class);
        } catch (Exception e) {
            throw new GptResponseParsingException(OpenAiErrorCode.GPT_RESPONSE_PARSING_ERROR);
        }
    }
}