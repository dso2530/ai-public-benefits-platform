package com.govtech.bff.clients;

import java.io.IOException;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.govtech.bff.auth.dto.InternalUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthenticationPropagationInterceptor
        implements ClientHttpRequestInterceptor {


    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution)
            throws IOException {

     Authentication auth =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        if (auth != null
                && auth.getPrincipal() instanceof InternalUser user) {

            request.getHeaders()
                    .setBearerAuth(user.token());
                }

        return execution.execute(request, body);
    }
}