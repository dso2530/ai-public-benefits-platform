package com.govtech.knowledge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.govtech")
@ConfigurationPropertiesScan("com.govtech")
public class KnowledgeServiceApplication {

    public static void main(String[] args) {

        SpringApplication.run(
                KnowledgeServiceApplication.class,
                args);

    }

}