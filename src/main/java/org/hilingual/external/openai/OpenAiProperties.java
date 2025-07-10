package org.hilingual.external.openai;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "openai")
public class OpenAiProperties {

    private String apiKey;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public HttpHeaders httpHeaders(OpenAiProperties openAiProperties) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + openAiProperties.getApiKey());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

}