package com.govtech.connectors.common.config;

import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.govtech.connectors.common.security.AuthenticationType;

@ConfigurationProperties(prefix = "connectors")
public record ConnectorProperties(

                String name,

                String baseUrl,

                AuthenticationType authentication,

                Duration connectTimeout,

                Duration readTimeout,

                Retry retry

) {

        public ConnectorProperties {

                name = name == null
                                ? "default"
                                : name;

                baseUrl = baseUrl == null
                                ? ""
                                : baseUrl;

                authentication = authentication == null
                                ? AuthenticationType.NONE
                                : authentication;

                connectTimeout = connectTimeout == null
                                ? Duration.ofSeconds(2)
                                : connectTimeout;

                readTimeout = readTimeout == null
                                ? Duration.ofSeconds(5)
                                : readTimeout;

                retry = retry == null
                                ? new Retry(
                                                true,
                                                3,
                                                Duration.ofSeconds(1))
                                : retry;

        }

        public record Retry(

                        boolean enabled,

                        int maxAttempts,

                        Duration delay

        ) {

                public Retry {

                        if (maxAttempts <= 0) {
                                maxAttempts = 1;
                        }

                        if (delay == null) {
                                delay = Duration.ofSeconds(1);
                        }

                }

        }

}