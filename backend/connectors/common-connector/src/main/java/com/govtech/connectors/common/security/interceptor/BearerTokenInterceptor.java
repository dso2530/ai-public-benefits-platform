package com.govtech.connectors.common.security.interceptor;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.govtech.connectors.common.security.oauth2.OAuth2TokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BearerTokenInterceptor
        implements ClientHttpRequestInterceptor {

    private final OAuth2TokenProvider tokenProvider;

    @Override
    public ClientHttpResponse intercept(

            HttpRequest request,

            byte[] body,

            ClientHttpRequestExecution execution)

            throws IOException {

        String token = tokenProvider.getAccessToken();

        request.getHeaders()
                .setBearerAuth(token);

        return execution.execute(
                request,
                body);

    }

}