package com.govtech.assistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.govtech")
public class AssistantBffApplication {

  public static void main(String[] args) {
    SpringApplication.run(AssistantBffApplication.class, args);
  }
}
