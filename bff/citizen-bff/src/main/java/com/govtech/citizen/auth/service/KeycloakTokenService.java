package com.govtech.citizen.auth.service;

import com.govtech.citizen.auth.dto.KeycloakTokenResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeycloakTokenService {

    private final RestClient oauthRestClient;

    public KeycloakTokenResponse exchangeCode(
            ClientRegistration registration,
            String code,
            String codeVerifier) {

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add(
                "grant_type",
                "authorization_code");

        form.add(
                "client_id",
                registration.getClientId());

        form.add(
                "code",
                code);

        form.add(
                "code_verifier",
                codeVerifier);

        form.add(
                "redirect_uri",
                registration.getRedirectUri());

        log.debug(
                "Calling Keycloak token endpoint: {}",
                registration
                        .getProviderDetails()
                        .getTokenUri());

        KeycloakTokenResponse response = oauthRestClient
                .post()
                .uri(
                        registration
                                .getProviderDetails()
                                .getTokenUri())
                .contentType(
                        MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(KeycloakTokenResponse.class);

        if (response == null) {

            log.error(
                    "Empty token response from Keycloak");

            throw new IllegalStateException(
                    "Empty token response from Keycloak");
        }

        if (response.idToken() == null
                || response.idToken().isBlank()) {

            log.error(
                    "Keycloak did not return an ID token");

            throw new IllegalStateException(
                    "Keycloak did not return an ID token");
        }

        log.info(
                "Keycloak token exchange succeeded");

        return response;
    }
}