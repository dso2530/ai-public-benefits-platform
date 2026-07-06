package com.govtech.connectors.common.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObservationConfiguration {

  @Bean
  public ObservationRegistry observationRegistry() {
    return ObservationRegistry.create();
  }
}
