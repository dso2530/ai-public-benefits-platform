package com.govtech.citizen.auth.service;

import com.govtech.bff.security.auth.model.InternalAuthentication;
import com.govtech.citizen.auth.dto.KeycloakIdentity;
import com.govtech.citizen.auth.dto.KeycloakTokenResponse;
import com.govtech.citizen.auth.dto.MobileLoginResponse;
import com.govtech.citizen.auth.dto.MobileTokenRequest;
import com.govtech.citizen.auth.dto.TokenResponse;
import com.govtech.citizen.auth.oauth.OAuthStateStore;
import com.govtech.citizen.auth.oauth.PkceChallenge;
import com.govtech.citizen.auth.oauth.PkceService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MobileOAuthService {

        private static final String REGISTRATION_ID = "citizen-mobile";

        private final PkceService pkceService;

        private final OAuthStateStore stateStore;

        private final ClientRegistrationRepository clientRegistrationRepository;

        private final KeycloakTokenService keycloakTokenService;

        private final KeycloakIdentityService keycloakIdentityService;

        private final InternalAuthenticationService authenticationService;

        public MobileLoginResponse createAuthorizationRequest() {

                ClientRegistration registration = getRegistration();

                String state = UUID.randomUUID().toString();

                PkceChallenge pkce = pkceService.generate();

                stateStore.save(
                                state,
                                pkce.codeVerifier());

                String authorizationUrl = registration
                                .getProviderDetails()
                                .getAuthorizationUri()

                                + "?client_id="
                                + encode(
                                                registration.getClientId())

                                + "&response_type=code"

                                + "&scope="
                                + encode(
                                                String.join(
                                                                " ",
                                                                registration.getScopes()))

                                + "&redirect_uri="
                                + encode(
                                                registration.getRedirectUri())

                                + "&state="
                                + encode(state)

                                + "&code_challenge="
                                + encode(pkce.codeChallenge())

                                + "&code_challenge_method=S256";

                log.info(
                                "Mobile OAuth authorization created: state={}",
                                state);

                return new MobileLoginResponse(
                                authorizationUrl,
                                state);
        }

        public TokenResponse exchange(
                        MobileTokenRequest request) {

                String state = request.state();

                log.info(
                                "Starting mobile OAuth exchange: state={}",
                                state);

                String codeVerifier = stateStore.get(state);

                if (codeVerifier == null) {

                        log.warn(
                                        "OAuth state not found or expired: state={}",
                                        state);

                        throw new IllegalStateException(
                                        "Invalid or expired OAuth state");
                }

                try {

                        ClientRegistration registration = getRegistration();

                        /*
                         * 1. Authorization code
                         * →
                         * Keycloak tokens
                         */
                        KeycloakTokenResponse keycloakToken = keycloakTokenService.exchangeCode(
                                        registration,
                                        request.code(),
                                        codeVerifier);

                        /*
                         * 2. Validation ID token
                         * →
                         * identité Keycloak
                         */
                        KeycloakIdentity identity = keycloakIdentityService.extractIdentity(
                                        keycloakToken.idToken());

                        /*
                         * 3. Identité Keycloak
                         * →
                         * identité interne BFF
                         */
                        InternalAuthentication internalAccessToken = authenticationService.authenticate(
                                        identity.subject(),
                                        identity.email(),
                                        identity.fullName());

                        log.info(
                                        "Internal authentication created: subject={}",
                                        identity.subject());

                        /*
                         * 4. On expose uniquement
                         * le JWT interne.
                         */
                        return new TokenResponse(
                                        internalAccessToken.accessToken(),
                                        "Bearer",
                                        internalAccessToken.expiresIn());

                } finally {

                        /*
                         * PKCE verifier = usage unique.
                         */
                        stateStore.remove(state);

                        log.debug(
                                        "OAuth state removed: state={}",
                                        state);
                }
        }

        public void logout(
                        Authentication authentication) {

                log.info(
                                "Mobile logout: authenticated={}, principal={}",
                                authentication != null
                                                && authentication.isAuthenticated(),
                                authentication != null
                                                ? authentication
                                                                .getPrincipal()
                                                                .getClass()
                                                                .getSimpleName()
                                                : "none");

                authenticationService.clear();
        }

        private ClientRegistration getRegistration() {

                ClientRegistration registration = clientRegistrationRepository
                                .findByRegistrationId(
                                                REGISTRATION_ID);

                if (registration == null) {

                        throw new IllegalStateException(
                                        "OAuth registration not found: "
                                                        + REGISTRATION_ID);
                }

                return registration;
        }

        private String encode(
                        String value) {

                return URLEncoder.encode(
                                value,
                                StandardCharsets.UTF_8);
        }
}