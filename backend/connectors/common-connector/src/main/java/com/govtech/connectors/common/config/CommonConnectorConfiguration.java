package com.govtech.connectors.common.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import com.govtech.connectors.common.scheduler.ConnectorSchedulerProperties;
import com.govtech.connectors.common.security.api.ApiKeyProperties;
import com.govtech.connectors.common.security.oauth2.OAuth2Properties;

@Configuration
@EnableConfigurationProperties({
    ConnectorProperties.class,
    ConnectorSchedulerProperties.class,
    OAuth2Properties.class,
    ApiKeyProperties.class

})

public class CommonConnectorConfiguration {

  @Bean
  public RestClient.Builder restClientBuilder() {
    return RestClient.builder();
  }
}
