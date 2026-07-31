package com.govtech.connectors.common.security.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.govtech.connectors.common.security.interceptor.BearerTokenInterceptor;
import com.govtech.connectors.common.security.oauth2.ClientCredentialsTokenProvider;
import com.govtech.connectors.common.security.oauth2.OAuth2Properties;
import com.govtech.connectors.common.security.oauth2.OAuth2TokenProvider;

@Configuration
@EnableConfigurationProperties(OAuth2Properties.class)
public class OAuth2Configuration {

  @Bean
  @ConditionalOnProperty(prefix = "connectors.security.oauth2", name = "enabled", havingValue = "true")
  public OAuth2TokenProvider oauth2TokenProvider(
      OAuth2Properties properties,
      RestClient.Builder builder) {

    return new ClientCredentialsTokenProvider(
        properties,
        builder);

  }

  @Bean
  @ConditionalOnProperty(prefix = "connectors.security.oauth2", name = "enabled", havingValue = "true")
  public BearerTokenInterceptor bearerTokenInterceptor(
      OAuth2TokenProvider provider) {

    return new BearerTokenInterceptor(provider);

  }

}