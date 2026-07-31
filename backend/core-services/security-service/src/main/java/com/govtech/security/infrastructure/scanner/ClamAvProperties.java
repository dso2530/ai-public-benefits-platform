package com.govtech.security.infrastructure.scanner;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "clamav")
public record ClamAvProperties(
        String host,
        int port,
        int timeout

) {
}