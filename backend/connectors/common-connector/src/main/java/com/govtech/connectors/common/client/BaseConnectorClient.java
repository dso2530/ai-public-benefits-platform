package com.govtech.connectors.common.client;

import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

import com.govtech.connectors.common.exception.ConnectorException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public abstract class BaseConnectorClient {

  protected final RestClient connectorRestClient;

  protected <T> T get(
      String uri,
      Class<T> responseType) {

    try {

      return connectorRestClient.get()
          .uri(uri)
          .retrieve()
          .onStatus(
              HttpStatusCode::isError,
              (request, response) -> {

                throw new ConnectorException(
                    "Connector HTTP error "
                        + response.getStatusCode());

              })
          .body(responseType);

    } catch (ConnectorException e) {

      throw e;

    } catch (Exception e) {

      log.error(
          "Connector call failed uri={}",
          uri,
          e);

      throw new ConnectorException(
          "Connector unavailable",
          e);
    }
  }

}