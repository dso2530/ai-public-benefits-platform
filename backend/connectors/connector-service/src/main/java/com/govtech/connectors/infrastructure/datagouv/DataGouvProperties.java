package com.govtech.connectors.infrastructure.datagouv;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "connectors.datagouv")
public record DataGouvProperties(

        String baseUrl,

        String organization,

        String territoryCode,

        String query,

        int limit

) {


    public DataGouvProperties {

        if (baseUrl == null || baseUrl.isBlank()) {
            baseUrl = "https://www.data.gouv.fr";
        }

        if (limit <= 0) {
            limit = 20;
        }

    }

}