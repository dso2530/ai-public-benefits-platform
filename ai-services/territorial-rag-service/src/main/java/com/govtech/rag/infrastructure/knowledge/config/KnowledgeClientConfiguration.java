package com.govtech.rag.infrastructure.knowledge.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class KnowledgeClientConfiguration {

        @Bean
        RestClient knowledgeRestClient(
                        @Value("${knowledge.service.url}") String url) {

                return RestClient.builder()
                                .baseUrl(url)
                                .requestFactory(
                                                new HttpComponentsClientHttpRequestFactory())
                                .build();

        }
}