package com.govtech.connectors.infrastructure.caf;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "connectors.caf")
public record CafProperties(

        boolean enabled,

        String baseUrl,

        String territoryCode,

        List<String> documents,

        int limit

) {

    public CafProperties {

        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://www.caf.fr";
        }

        if (territoryCode == null || territoryCode.isBlank()) {
            territoryCode = "FR";
        }

        if (documents == null) {
            documents = List.of();
        }

        if (limit <= 0) {
            limit = 20;
        }
    }
}