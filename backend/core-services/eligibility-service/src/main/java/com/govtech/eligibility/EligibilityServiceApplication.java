package com.govtech.eligibility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.govtech")
@ConfigurationPropertiesScan("com.govtech")
@EntityScan(basePackages = {
    "com.govtech.eligibility.infrastructure.persistence",
    "com.govtech.platform.database.outbox"
})
@EnableJpaRepositories(basePackages = {
    "com.govtech.eligibility.infrastructure.persistence",
    "com.govtech.platform.database.outbox"
})
public class EligibilityServiceApplication {

  public static void main(String[] args) {

    SpringApplication.run(EligibilityServiceApplication.class, args);
  }
}
