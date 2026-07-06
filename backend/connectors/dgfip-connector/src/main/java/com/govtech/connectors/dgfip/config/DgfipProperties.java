package com.govtech.connectors.dgfip.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "dgfip")
public record DgfipProperties(String baseUrl, OAuth2 oauth2, Api api) {

  public record OAuth2(String tokenUri, String clientId, String clientSecret, String scope) {}

  public record Api(String income, String latestTaxNotice) {}
}
