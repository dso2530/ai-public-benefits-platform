package com.govtech.connectors.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.govtech.connectors.common.security.api.ApiKeyProperties;
import com.govtech.connectors.common.security.api.ApiKeyProvider;
import com.govtech.connectors.common.security.api.DefaultApiKeyProvider;
import com.govtech.connectors.common.security.interceptor.ApiKeyInterceptor;

@Configuration
@EnableConfigurationProperties(ApiKeyProperties.class)
public class ApiKeyConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "connectors.security.api-key", name = "enabled", havingValue = "true")
    public ApiKeyProvider apiKeyProvider(
            ApiKeyProperties properties) {

        return new DefaultApiKeyProvider(properties);

    }

    @Bean
    @ConditionalOnProperty(prefix = "connectors.security.api-key", name = "enabled", havingValue = "true")
    public ApiKeyInterceptor apiKeyInterceptor(
            ApiKeyProvider provider,
            ApiKeyProperties properties) {

        return new ApiKeyInterceptor(
                provider,
                properties);

    }

}