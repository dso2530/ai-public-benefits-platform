package com.govtech.citizen.clients;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.govtech.citizen.dashboard.dto.EligibilitySummaryDto;
import com.govtech.citizen.eligibility.dto.EligibilityDto;
import com.govtech.platform.web.error.ExternalServiceUnavailableException;
import com.govtech.platform.web.rest.RestClientErrorHandler;

@Component
@RequiredArgsConstructor
public class EligibilityClient {

  private static final String SERVICE_NAME = "eligibility-service";

  private final RestClient restClient;

  @Value("${services.eligibility.url}")
  private String eligibilityUrl;

  public EligibilitySummaryDto getSummary() {

    try {

      return restClient
          .get()
          .uri(
              eligibilityUrl
                  + "/api/eligibility/summary")
          .retrieve()
          .body(EligibilitySummaryDto.class);

    } catch (RestClientResponseException ex) {

      throw RestClientErrorHandler.handle(
          ex,
          SERVICE_NAME);

    } catch (ResourceAccessException ex) {

      throw serviceUnavailable(ex);
    }
  }

  public List<EligibilityDto> eligibilities() {

    try {

      return restClient
          .get()
          .uri(
              eligibilityUrl
                  + "/api/eligibility")
          .retrieve()
          .body(
              new ParameterizedTypeReference<List<EligibilityDto>>() {
              });

    } catch (RestClientResponseException ex) {

      throw RestClientErrorHandler.handle(
          ex,
          SERVICE_NAME);

    } catch (ResourceAccessException ex) {

      throw serviceUnavailable(ex);
    }
  }

  private ExternalServiceUnavailableException serviceUnavailable(
      ResourceAccessException ex) {

    return new ExternalServiceUnavailableException(
        "Le service " + SERVICE_NAME
            + " est indisponible.",
        ex);
  }
}
