package com.govtech.connectors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(scanBasePackages = "com.govtech")
@ConfigurationPropertiesScan("com.govtech")
public class ConnectorServiceApplication {

        public static void main(String[] args) {

                SpringApplication.run(
                                ConnectorServiceApplication.class,
                                args);

        }

}