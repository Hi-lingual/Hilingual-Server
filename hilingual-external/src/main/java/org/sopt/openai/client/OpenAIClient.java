package org.sopt.openai.client;

import org.sopt.openai.OpenAIConstant;
import org.sopt.openai.dto.req.GptRequest;
import org.sopt.openai.dto.res.GptResponse;
import org.sopt.openai.feign.OpenAIFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "openAIClient", url = OpenAIConstant.OPENAI_URL, configuration = OpenAIFeignConfig.class)
@Component
public interface OpenAIClient {

    @PostMapping
    GptResponse callChatCompletion(GptRequest gptRequest);

}