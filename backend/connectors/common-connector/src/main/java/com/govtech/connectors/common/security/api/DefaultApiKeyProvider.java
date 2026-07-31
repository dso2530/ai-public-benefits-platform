package com.govtech.connectors.common.security.api;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DefaultApiKeyProvider
        implements ApiKeyProvider {

    private final ApiKeyProperties properties;

    @Override
    public String getApiKey() {

        return properties.value();

    }

}