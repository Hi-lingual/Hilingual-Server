package org.hilingual.external.openai.client;

import org.hilingual.external.feign.OpenAIFeignConfig;
import org.hilingual.external.openai.OpenAIConstant;
import org.hilingual.external.openai.dto.req.GptRequest;
import org.hilingual.external.openai.dto.res.GptResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "openAIClient", url = OpenAIConstant.OPENAI_URL, configuration = OpenAIFeignConfig.class)
@Component
public interface OpenAIClient {

    @PostMapping
    GptResponse callChatCompletion(GptRequest gptRequest);

}