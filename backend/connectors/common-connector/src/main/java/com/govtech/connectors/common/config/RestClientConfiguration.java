package com.govtech.connectors.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
@RequiredArgsConstructor
public class RestClientConfiguration {

  private final ConnectorProperties properties;

  @Bean
  RestClient restClient(RestClient.Builder builder) {

    return builder.requestFactory(clientHttpRequestFactory()).build();
  }

  private ClientHttpRequestFactory clientHttpRequestFactory() {

    var factory = new SimpleClientHttpRequestFactory();

    factory.setConnectTimeout(properties.connectTimeout());
    factory.setReadTimeout(properties.readTimeout());

    return factory;
  }
}
