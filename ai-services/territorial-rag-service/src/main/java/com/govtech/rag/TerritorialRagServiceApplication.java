package com.govtech.rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.govtech")
public class TerritorialRagServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                TerritorialRagServiceApplication.class,
                args);

    }

}