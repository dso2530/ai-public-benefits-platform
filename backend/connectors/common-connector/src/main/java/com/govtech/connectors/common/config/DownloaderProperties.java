package com.govtech.connectors.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "connectors.downloader")
public record DownloaderProperties(

        String userAgent,

        int maxLinks

) {

    public DownloaderProperties {

        userAgent = userAgent == null
                ? "GovTech-Connector/1.0"
                : userAgent;

        if (maxLinks <= 0) {
            maxLinks = 100;
        }

    }

}