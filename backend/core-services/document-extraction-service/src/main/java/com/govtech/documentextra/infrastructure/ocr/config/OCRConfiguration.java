package com.govtech.documentextra.infrastructure.ocr.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class OCRConfiguration {

    @Bean
    public RestClient paddleOcrRestClient(
            @Value("${ocr.base-url}") String baseUrl) {

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();

        requestFactory.setReadTimeout(Duration.ofMinutes(5));
        requestFactory.setConnectionRequestTimeout(Duration.ofSeconds(10));

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .requestInterceptor((request, body, execution) -> execution.execute(request, body))
                .build();
    }
}