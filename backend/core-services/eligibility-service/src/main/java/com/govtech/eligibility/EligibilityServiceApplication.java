package com.govtech.eligibility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.govtech")
@ConfigurationPropertiesScan("com.govtech")
public class EligibilityServiceApplication {

  public static void main(String[] args) {

    SpringApplication.run(EligibilityServiceApplication.class, args);
  }
}
