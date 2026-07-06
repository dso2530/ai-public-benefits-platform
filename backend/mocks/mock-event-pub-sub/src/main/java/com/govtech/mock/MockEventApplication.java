package com.govtech.mock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.govtech")
public class MockEventApplication {

    public static void main(String[] args) {
        SpringApplication.run(MockEventApplication.class, args);
    }
}