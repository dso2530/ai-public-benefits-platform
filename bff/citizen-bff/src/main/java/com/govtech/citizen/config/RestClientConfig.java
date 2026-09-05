package com.govtech.citizen.config;

import java.time.Duration;

import lombok.RequiredArgsConstructor;

import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.util.Timeout;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.govtech.citizen.interceptor.AuthenticationPropagationInterceptor;
import com.govtech.platform.web.correlation.CorrelationIdInterceptor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    private final AuthenticationPropagationInterceptor bearerTokenInterceptor;
    private final CorrelationIdInterceptor correlationIdInterceptor;

    private static final Duration CONNECT_TIMEOUT = Duration.ofSeconds(10);

    private static final Duration CONNECTION_REQUEST_TIMEOUT = Duration.ofSeconds(10);

    /*
     * Timeout d'attente de la réponse du RAG.
     *
     * Temporairement à 5 minutes car Ollama fonctionne
     * actuellement sur CPU et peut prendre plus de 3 minutes.
     */
    private static final Duration RESPONSE_TIMEOUT = Duration.ofMinutes(5);

    @Bean
    public RestClient restClient() {

        return RestClient.builder()
                .requestInterceptor(bearerTokenInterceptor)
                .requestInterceptor(correlationIdInterceptor)
                .requestFactory(httpRequestFactory())
                .build();
    }

    @Bean
    public RestClient oauthRestClient() {

        return RestClient.builder()
                .requestInterceptor(correlationIdInterceptor)
                .requestFactory(httpRequestFactory())
                .build();
    }

    private HttpComponentsClientHttpRequestFactory httpRequestFactory() {

        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(
                        Timeout.ofMilliseconds(
                                CONNECT_TIMEOUT.toMillis()))
                .setConnectionRequestTimeout(
                        Timeout.ofMilliseconds(
                                CONNECTION_REQUEST_TIMEOUT.toMillis()))
                .setResponseTimeout(
                        Timeout.ofMilliseconds(
                                RESPONSE_TIMEOUT.toMillis()))
                .build();

        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();

        return new HttpComponentsClientHttpRequestFactory(
                httpClient);
    }

}
