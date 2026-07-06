package com.govtech.connectors.dgfip.config;

import com.govtech.connectors.common.config.OAuth2TokenProvider;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.web.client.RestClient;

@Configuration
@EnableConfigurationProperties(DgfipProperties.class)
public class DgfipConfiguration {

  @Bean
  ClientRegistration dgfipClientRegistration(DgfipProperties props) {
    return ClientRegistration.withRegistrationId("dgfip")
        .tokenUri(props.oauth2().tokenUri())
        .clientId(props.oauth2().clientId())
        .clientSecret(props.oauth2().clientSecret())
        .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
        .scope(props.oauth2().scope().split(","))
        .build();
  }

  @Bean
  RestClient dgfipRestClient(
      RestClient.Builder builder,
      OAuth2TokenProvider tokenProvider,
      @Value("${dgfip.base-url}") String baseUrl) {

    return builder
        .baseUrl(baseUrl)
        .requestFactory(clientHttpRequestFactory())
        .requestInterceptor(
            (request, body, execution) -> {
              request.getHeaders().setBearerAuth(tokenProvider.getAccessToken("dgfip"));

              return execution.execute(request, body);
            })
        .build();
  }

  private ClientHttpRequestFactory clientHttpRequestFactory() {

    var factory = new SimpleClientHttpRequestFactory();

    factory.setConnectTimeout(Duration.ofMillis(2000));
    factory.setReadTimeout(Duration.ofMillis(2000));

    return factory;
  }
}
