package com.govtech.bff.config;

import com.govtech.bff.clients.AuthenticationPropagationInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

  private final AuthenticationPropagationInterceptor bearerTokenInterceptor;

  @Bean
  public RestClient restClient() {
    return RestClient.builder()
        .requestInterceptor(bearerTokenInterceptor)
        .requestFactory(
            new HttpComponentsClientHttpRequestFactory()) // Personnalisation de la requête HTTP
        .build();
  }
}
