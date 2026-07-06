package com.govtech.ocr.infrastructure.config;

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

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(
                        new HttpComponentsClientHttpRequestFactory())
                .requestInterceptor((request, body, execution) -> {
                    System.out.println(request.getMethod() + " " + request.getURI());
                    System.out.println(request.getHeaders());
                    return execution.execute(request, body);
                })
                .build();
    }
}