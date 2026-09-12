package com.govtech.profile;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.persistence.autoconfigure.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.govtech")
@EntityScan(basePackages = {
    "com.govtech.profile.infrastructure.persistence",
    "com.govtech.platform.database.outbox"
})
@EnableJpaRepositories(basePackages = {
    "com.govtech.profile.infrastructure.persistence",
    "com.govtech.platform.database.outbox"
})
public class ProfileServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProfileServiceApplication.class, args);
  }
}
