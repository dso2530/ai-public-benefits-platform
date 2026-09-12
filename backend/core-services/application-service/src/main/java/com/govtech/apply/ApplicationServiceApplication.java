package com.govtech.apply;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.govtech")
@ConfigurationPropertiesScan("com.govtech")
@EntityScan(basePackages = {
    "com.govtech.apply.infrastructure.persistence",
    "com.govtech.platform.database.outbox"
})
@EnableJpaRepositories(basePackages = {
    "com.govtech.apply.infrastructure.persistence",
    "com.govtech.platform.database.outbox"
})
public class ApplicationServiceApplication {

  public static void main(String[] args) {

    SpringApplication.run(ApplicationServiceApplication.class, args);
  }
}
