package com.govtech.connectors.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.*;

@Configuration
public class OAuth2Configuration {

  @Bean
  ClientRegistrationRepository clientRegistrationRepository(ClientRegistration registration) {

    return new InMemoryClientRegistrationRepository(registration);
  }

  @Bean
  OAuth2AuthorizedClientService authorizedClientService(ClientRegistrationRepository repository) {

    return new InMemoryOAuth2AuthorizedClientService(repository);
  }

  @Bean
  OAuth2AuthorizedClientManager authorizedClientManager(
      ClientRegistrationRepository repository, OAuth2AuthorizedClientService service) {

    AuthorizedClientServiceOAuth2AuthorizedClientManager manager =
        new AuthorizedClientServiceOAuth2AuthorizedClientManager(repository, service);

    manager.setAuthorizedClientProvider(
        OAuth2AuthorizedClientProviderBuilder.builder().clientCredentials().build());

    return manager;
  }
}
