package com.govtech.connectors.common.config;

import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "connector")
public record ConnectorProperties(Duration connectTimeout, Duration readTimeout, Retry retry) {

  public ConnectorProperties {
    connectTimeout = connectTimeout == null ? Duration.ofSeconds(2) : connectTimeout;

    readTimeout = readTimeout == null ? Duration.ofSeconds(2) : readTimeout;

    retry = retry == null ? new Retry(3, 100) : retry;
  }

  public record Retry(int maxAttempts, long delay) {}
}
