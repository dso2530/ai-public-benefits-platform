package com.govtech.ocr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.govtech")
@ConfigurationPropertiesScan("com.govtech")
public class OcrServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OcrServiceApplication.class, args);
    }
}