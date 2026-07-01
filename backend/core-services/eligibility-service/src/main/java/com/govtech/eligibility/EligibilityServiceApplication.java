package com.govtech.eligibility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.govtech")
public class EligibilityServiceApplication {

  public static void main(String[] args) {

    SpringApplication.run(EligibilityServiceApplication.class, args);
  }
}
