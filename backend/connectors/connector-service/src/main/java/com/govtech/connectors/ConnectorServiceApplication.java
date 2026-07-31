package com.govtech.connectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = "com.govtech")
@ConfigurationPropertiesScan("com.govtech")
@EnableScheduling
public class ConnectorServiceApplication {

        public static void main(String[] args) {

                SpringApplication.run(
                                ConnectorServiceApplication.class,
                                args);

        }

}