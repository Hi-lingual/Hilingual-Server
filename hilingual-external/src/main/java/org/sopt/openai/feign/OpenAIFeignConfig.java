package org.sopt.openai.feign;

import feign.RequestInterceptor;
import lombok.RequiredArgsConstructor;
import org.sopt.openai.OpenAIProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;

@Configuration
@EnableConfigurationProperties(OpenAIProperties.class)
@RequiredArgsConstructor
public class OpenAIFeignConfig {

    private final OpenAIProperties openAIProperties;

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + openAIProperties.getApiKey());
            requestTemplate.header("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        };
    }
}