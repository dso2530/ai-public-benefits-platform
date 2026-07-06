package com.govtech.connectors.dgfip.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.govtech.connectors.common.exception.ConnectorAccessDeniedException;
import com.govtech.connectors.common.exception.ConnectorAuthenticationException;
import com.govtech.connectors.common.exception.ConnectorNotFoundException;
import com.govtech.connectors.common.exception.ConnectorRateLimitException;
import com.govtech.connectors.common.exception.ConnectorServerException;
import com.govtech.connectors.common.exception.ConnectorTimeoutException;
import com.govtech.connectors.dgfip.TestApplication;
import com.govtech.shared.model.Income;
import com.govtech.shared.model.TaxNotice;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(classes = TestApplication.class)
@ActiveProfiles("test")
class DgfipConnectorServiceIT {

  @Autowired private DgfipConnectorService service;

  @Test
  void shouldReturnIncome() {

    Income income = service.getIncome("123456789");

    assertThat(income).isNotNull();
    assertThat(income.fiscalYear()).isEqualTo("2025");
    assertThat(income.annualIncome()).isEqualByComparingTo("28500.00");
  }

  @Test
  void shouldReturnTaxNotice() {

    TaxNotice taxNotice = service.getLatestTaxNotice("123456789");

    assertThat(taxNotice).isNotNull();
    assertThat(taxNotice.referenceNumber()).startsWith("AV-");
  }

  @Test
  void shouldThrowUnauthorizedException() {

    assertThatThrownBy(() -> service.getIncome("401"))
        .isInstanceOf(ConnectorAuthenticationException.class);
  }

  @Test
  void shouldThrowForbiddenException() {

    assertThatThrownBy(() -> service.getIncome("403"))
        .isInstanceOf(ConnectorAccessDeniedException.class);
  }

  @Test
  void shouldThrowNotFoundException() {

    assertThatThrownBy(() -> service.getIncome("404"))
        .isInstanceOf(ConnectorNotFoundException.class);
  }

  @Test
  void shouldThrowTooManyRequestsException() {

    assertThatThrownBy(() -> service.getIncome("429"))
        .isInstanceOf(ConnectorRateLimitException.class);
  }

  @Test
  void shouldThrowServerException() {

    assertThatThrownBy(() -> service.getIncome("500")).isInstanceOf(ConnectorServerException.class);
  }

  @Test
  void shouldTimeout() {

    assertThatThrownBy(() -> service.getIncome("TIMEOUT"))
        .isInstanceOf(ConnectorTimeoutException.class);
  }

  @Test
  void shouldRetryAndEventuallySucceed() {

    Income income = service.getIncome("RETRY");

    assertThat(income).isNotNull();
    assertThat(income.fiscalYear()).isEqualTo("2025");
  }

  @Test
  void shouldFailOnInvalidJson() {

    assertThatThrownBy(() -> service.getIncome("INVALID_JSON")).isInstanceOf(Exception.class);
  }
}
