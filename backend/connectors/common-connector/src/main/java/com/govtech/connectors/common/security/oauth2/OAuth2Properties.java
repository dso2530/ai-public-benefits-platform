package com.govtech.connectors.common.security.oauth2;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "connectors.security.oauth2")
public record OAuth2Properties(

                boolean enabled,

                String tokenUri,

                String clientId,

                String clientSecret,

                List<String> scopes

) {
}