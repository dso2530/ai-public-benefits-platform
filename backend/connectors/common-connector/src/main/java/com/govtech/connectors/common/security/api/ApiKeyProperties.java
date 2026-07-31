package com.govtech.connectors.common.security.api;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "connectors.security.api-key")
public record ApiKeyProperties(

                boolean enabled,

                String headerName,

                String value

) {

        public ApiKeyProperties {

                if (headerName == null || headerName.isBlank()) {

                        headerName = "X-API-Key";

                }

        }

}