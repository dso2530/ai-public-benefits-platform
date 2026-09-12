
package com.govtech.extraction.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class LLMConfiguration {

    @Bean
    RestClient llmRestClient(
            @Value("${llm.base-url}") String baseUrl) {

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(
                        new HttpComponentsClientHttpRequestFactory())
                .build();
    }
}
