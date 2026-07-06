package com.govtech.connectors.dgfip.service;

import com.govtech.connectors.common.exception.ConnectorAccessDeniedException;
import com.govtech.connectors.common.exception.ConnectorAuthenticationException;
import com.govtech.connectors.common.exception.ConnectorNotFoundException;
import com.govtech.connectors.common.exception.ConnectorRateLimitException;
import com.govtech.connectors.common.exception.ConnectorServerException;
import com.govtech.connectors.common.exception.ConnectorTimeoutException;
import com.govtech.connectors.dgfip.client.DgfipClient;
import com.govtech.connectors.dgfip.dto.DgfipIncomeResponse;
import com.govtech.connectors.dgfip.dto.DgfipTaxNoticeResponse;
import com.govtech.connectors.dgfip.mapper.DgfipMapper;
import com.govtech.shared.model.Income;
import com.govtech.shared.model.TaxNotice;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DgfipConnectorService {

  private final DgfipClient dgfipClient;
  private final DgfipMapper mapper;

  @Retryable(
      retryFor = {ConnectorServerException.class, ConnectorTimeoutException.class},
      noRetryFor = {
        ConnectorAuthenticationException.class,
        ConnectorAccessDeniedException.class,
        ConnectorNotFoundException.class,
        ConnectorRateLimitException.class
      },
      maxAttemptsExpression = "${connector.retry.max-attempts}",
      backoff = @Backoff(delayExpression = "${connector.retry.delay}"))
  public Income getIncome(String fiscalNumber) {

    DgfipIncomeResponse response = dgfipClient.getIncome(fiscalNumber);

    return mapper.toIncome(response);
  }

  @Retryable(
      retryFor = {ConnectorServerException.class, ConnectorTimeoutException.class},
      noRetryFor = {
        ConnectorAuthenticationException.class,
        ConnectorAccessDeniedException.class,
        ConnectorNotFoundException.class,
        ConnectorRateLimitException.class
      },
      maxAttemptsExpression = "${connector.retry.max-attempts}",
      backoff = @Backoff(delayExpression = "${connector.retry.delay}"))
  public TaxNotice getLatestTaxNotice(String fiscalNumber) {

    DgfipTaxNoticeResponse response = dgfipClient.getLatestTaxNotice(fiscalNumber);

    return mapper.toTaxNotice(response);
  }
}
