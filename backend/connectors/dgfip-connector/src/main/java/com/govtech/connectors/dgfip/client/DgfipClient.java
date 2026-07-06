package com.govtech.connectors.dgfip.client;

import com.govtech.connectors.common.client.BaseConnectorClient;
import com.govtech.connectors.common.config.OAuth2TokenProvider;
import com.govtech.connectors.common.exception.ConnectorAccessDeniedException;
import com.govtech.connectors.common.exception.ConnectorAuthenticationException;
import com.govtech.connectors.common.exception.ConnectorNotFoundException;
import com.govtech.connectors.common.exception.ConnectorRateLimitException;
import com.govtech.connectors.common.exception.ConnectorServerException;
import com.govtech.connectors.common.exception.ConnectorTimeoutException;
import com.govtech.connectors.common.exception.ConnectorUnavailableException;
import com.govtech.connectors.dgfip.dto.DgfipIncomeResponse;
import com.govtech.connectors.dgfip.dto.DgfipTaxNoticeResponse;
import org.springframework.retry.annotation.Recover;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

@Component
public class DgfipClient extends BaseConnectorClient {

  public DgfipClient(RestClient dgfipRestClient, OAuth2TokenProvider tokenProvider) {
    super(dgfipRestClient, tokenProvider);
  }

  public DgfipIncomeResponse getIncome(String fiscalNumber) {

    try {
      return restClient
          .get()
          .uri("/income/{id}", fiscalNumber)
          .retrieve()
          .onStatus(
              status -> status.value() == 401,
              (request, response) -> {
                throw new ConnectorAuthenticationException("Unauthorized");
              })
          .onStatus(
              status -> status.value() == 403,
              (request, response) -> {
                throw new ConnectorAccessDeniedException("Forbidden");
              })
          .onStatus(
              status -> status.value() == 404,
              (request, response) -> {
                throw new ConnectorNotFoundException("Citizen not found");
              })
          .onStatus(
              status -> status.value() == 429,
              (request, response) -> {
                throw new ConnectorRateLimitException("Too many requests");
              })
          .onStatus(
              status -> status.is5xxServerError(),
              (request, response) -> {
                throw new ConnectorServerException("DGFiP server error");
              })
          .body(DgfipIncomeResponse.class);

    } catch (ResourceAccessException e) {
      throw new ConnectorTimeoutException("Timeout while calling DGFiP API", e);
    }
  }

  public DgfipTaxNoticeResponse getLatestTaxNotice(String fiscalNumber) {

    try {
      return restClient
          .get()
          .uri("/tax-notices/latest/{id}", fiscalNumber)
          .retrieve()
          .onStatus(
              status -> status.value() == 401,
              (request, response) -> {
                throw new ConnectorAuthenticationException("Unauthorized access to DGFiP API");
              })
          .onStatus(
              status -> status.value() == 403,
              (request, response) -> {
                throw new ConnectorAccessDeniedException("Access denied to DGFiP API");
              })
          .onStatus(
              status -> status.value() == 404,
              (request, response) -> {
                throw new ConnectorNotFoundException("Tax notice not found");
              })
          .onStatus(
              status -> status.value() == 429,
              (request, response) -> {
                throw new ConnectorRateLimitException("Too many requests");
              })
          .onStatus(
              status -> status.is5xxServerError(),
              (request, response) -> {
                throw new ConnectorServerException("DGFiP API server error");
              })
          .body(DgfipTaxNoticeResponse.class);
    } catch (ResourceAccessException e) {
      throw new ConnectorTimeoutException("Timeout while calling DGFiP API", e);
    }
  }

  @Recover
  public DgfipIncomeResponse recoverIncome(ConnectorServerException ex, String fiscalNumber) {
    throw new ConnectorUnavailableException("DGFiP API unavailable after retries", ex);
  }

  @Recover
  public DgfipIncomeResponse recoverIncome(ConnectorTimeoutException ex, String fiscalNumber) {
    throw new ConnectorUnavailableException("DGFiP API unavailable after retries", ex);
  }

  @Recover
  public DgfipTaxNoticeResponse recoverTaxNotice(ConnectorServerException ex, String fiscalNumber) {
    throw new ConnectorUnavailableException("DGFiP API unavailable after retries", ex);
  }

  @Recover
  public DgfipTaxNoticeResponse recoverTaxNotice(
      ConnectorTimeoutException ex, String fiscalNumber) {
    throw new ConnectorUnavailableException("DGFiP API unavailable after retries", ex);
  }
}
