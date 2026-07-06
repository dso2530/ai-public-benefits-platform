package com.govtech.connectors.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2TokenProvider {

  private final OAuth2AuthorizedClientManager clientManager;

  public String getAccessToken(String registrationId) {

    OAuth2AuthorizeRequest request =
        OAuth2AuthorizeRequest.withClientRegistrationId(registrationId)
            .principal(
                new AnonymousAuthenticationToken(
                    registrationId,
                    registrationId,
                    AuthorityUtils.createAuthorityList("ROLE_SYSTEM")))
            .build();

    OAuth2AuthorizedClient client = clientManager.authorize(request);

    if (client == null) {
      throw new IllegalStateException("Unable to obtain OAuth2 access token for " + registrationId);
    }

    return client.getAccessToken().getTokenValue();
  }
}
