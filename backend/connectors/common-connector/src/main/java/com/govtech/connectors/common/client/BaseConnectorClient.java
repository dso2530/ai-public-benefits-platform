package com.govtech.connectors.common.client;

import com.govtech.connectors.common.config.OAuth2TokenProvider;
import lombok.Getter;
import org.springframework.web.client.RestClient;

@Getter
public abstract class BaseConnectorClient {

  protected final RestClient restClient;
  protected final OAuth2TokenProvider tokenProvider;

  protected BaseConnectorClient(RestClient restClient, OAuth2TokenProvider tokenProvider) {
    this.restClient = restClient;
    this.tokenProvider = tokenProvider;
  }
}
