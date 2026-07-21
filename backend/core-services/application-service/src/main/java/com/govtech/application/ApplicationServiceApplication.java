package com.govtech.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.govtech")
public class ApplicationServiceApplication {

  public static void main(String[] args) {

    SpringApplication.run(ApplicationServiceApplication.class, args);
  }
}
