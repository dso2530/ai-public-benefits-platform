package com.govtech.citizen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.govtech")
public class CitizenBffApplication {

  public static void main(String[] args) {
    SpringApplication.run(CitizenBffApplication.class, args);
  }
}
