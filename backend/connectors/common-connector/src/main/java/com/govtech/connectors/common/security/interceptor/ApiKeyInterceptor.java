package com.govtech.connectors.common.security.interceptor;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import com.govtech.connectors.common.security.api.ApiKeyProperties;
import com.govtech.connectors.common.security.api.ApiKeyProvider;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class ApiKeyInterceptor
                implements ClientHttpRequestInterceptor {

        private final ApiKeyProvider provider;

        private final ApiKeyProperties properties;

        @Override
        public ClientHttpResponse intercept(
                        HttpRequest request,
                        byte[] body,
                        ClientHttpRequestExecution execution)
                        throws IOException {

                request.getHeaders()
                                .add(
                                                properties.headerName(),
                                                provider.getApiKey());

                return execution.execute(
                                request,
                                body);

        }

}