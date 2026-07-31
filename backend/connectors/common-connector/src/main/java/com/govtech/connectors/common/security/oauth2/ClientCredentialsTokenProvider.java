package com.govtech.connectors.common.security.oauth2;

import java.time.Instant;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ClientCredentialsTokenProvider
        implements OAuth2TokenProvider {

    private final OAuth2Properties properties;

    private final RestClient.Builder restClientBuilder;

    private String accessToken;

    private Instant expiresAt;

    @Override
    public synchronized String getAccessToken() {

        if (isTokenValid()) {

            return accessToken;

        }

        log.info(
                "Requesting OAuth2 access token uri={}",
                properties.tokenUri());

        RestClient client = restClientBuilder.build();

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add(
                "grant_type",
                "client_credentials");

        List<String> scopes = properties.scopes();

        if (scopes != null && !scopes.isEmpty()) {

            form.add(
                    "scope",
                    String.join(
                            " ",
                            scopes));

        }

        TokenResponse response = client.post()

                .uri(
                        properties.tokenUri())

                .contentType(
                        MediaType.APPLICATION_FORM_URLENCODED)

                .headers(headers -> headers.setBasicAuth(
                        properties.clientId(),
                        properties.clientSecret()))

                .body(form)

                .retrieve()

                .body(TokenResponse.class);

        if (response == null
                || response.accessToken() == null) {

            throw new IllegalStateException(
                    "OAuth2 token response empty");

        }

        this.accessToken = response.accessToken();

        long expiresIn = Math.max(
                response.expiresIn(),
                60);

        this.expiresAt = Instant.now()
                .plusSeconds(
                        expiresIn - 30);

        log.info(
                "OAuth2 token acquired expiresIn={}s",
                expiresIn);

        return accessToken;

    }

    private boolean isTokenValid() {

        return accessToken != null
                && expiresAt != null
                && Instant.now()
                        .isBefore(expiresAt);

    }

    private record TokenResponse(

            String accessToken,

            long expiresIn,

            String tokenType

    ) {
    }

}