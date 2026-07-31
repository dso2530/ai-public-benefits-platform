package com.govtech.consent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.govtech")
public class ConsentServiceApplication {

  public static void main(String[] args) {

    SpringApplication.run(ConsentServiceApplication.class, args);
  }
}
