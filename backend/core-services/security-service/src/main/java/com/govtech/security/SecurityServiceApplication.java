package com.govtech.security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.govtech")
@ConfigurationPropertiesScan("com.govtech")
public class SecurityServiceApplication {

  public static void main(String[] args) {

    SpringApplication.run(SecurityServiceApplication.class, args);
  }
}
