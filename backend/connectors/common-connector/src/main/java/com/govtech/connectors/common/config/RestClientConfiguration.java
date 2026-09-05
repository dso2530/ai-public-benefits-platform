package com.govtech.connectors.common.config;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import com.govtech.connectors.common.interceptor.ObservationInterceptor;
import com.govtech.connectors.common.security.interceptor.ApiKeyInterceptor;
import com.govtech.connectors.common.security.interceptor.BearerTokenInterceptor;
import com.govtech.platform.web.correlation.CorrelationIdInterceptor;

@Configuration
@RequiredArgsConstructor
public class RestClientConfiguration {

    private final ConnectorProperties properties;

    @Bean
    RestClient connectorRestClient(
            RestClient.Builder builder,

            ObjectProvider<ApiKeyInterceptor> apiKeyInterceptor,

            ObjectProvider<BearerTokenInterceptor> bearerInterceptor,

            CorrelationIdInterceptor correlationInterceptor,

            ObservationInterceptor observationInterceptor) {

        RestClient.Builder client = builder
                .requestFactory(
                        clientHttpRequestFactory());

        apiKeyInterceptor.ifAvailable(
                client::requestInterceptor);

        bearerInterceptor.ifAvailable(
                client::requestInterceptor);

        client
                .requestInterceptor(
                        correlationInterceptor)
                .requestInterceptor(
                        observationInterceptor);

        return client.build();
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {

        var factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(
                properties.connectTimeout());

        factory.setReadTimeout(
                properties.readTimeout());

        return factory;
    }

}