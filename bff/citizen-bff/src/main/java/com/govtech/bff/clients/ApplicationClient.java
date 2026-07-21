package com.govtech.bff.clients;

import com.govtech.bff.application.dto.ApplicationDto;
import com.govtech.bff.application.dto.ApplicationStatus;
import com.govtech.bff.dashboard.dto.ApplicationsSummaryDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ApplicationClient {

  private final RestClient restClient;

  @Value("${services.application.url}")
  private String applicationUrl;

  public ApplicationsSummaryDto getSummary() {

    List<ApplicationDto> applications = retrieveAlls();

    return ApplicationsSummaryDto.builder()
        .total(applications.size())
        .generated(count(applications, ApplicationStatus.GENERATED))
        .readyToSubmit(count(applications, ApplicationStatus.READY_TO_SUBMIT))
        .submitted(count(applications, ApplicationStatus.SUBMITTED))
        .accepted(count(applications, ApplicationStatus.ACCEPTED))
        .rejected(count(applications, ApplicationStatus.REJECTED))
        .build();
  }

  private int count(List<ApplicationDto> applications, ApplicationStatus status) {
    return (int) applications.stream().filter(a -> a.status() == status).count();
  }

  public List<ApplicationDto> retrieveAlls() {
    return restClient
        .get()
        .uri(applicationUrl + "/applications")
        .retrieve()
        .body(new ParameterizedTypeReference<List<ApplicationDto>>() {});
  }

  public ApplicationDto findById(UUID id) {
    return restClient
        .get()
        .uri(applicationUrl + "/applications/{id}", id)
        .retrieve()
        .body(ApplicationDto.class);
  }

  public void submit(UUID id) {
    restClient
        .post()
        .uri(applicationUrl + "/applications/{id}/submit", id)
        .retrieve()
        .toBodilessEntity();
  }

  public byte[] download(UUID id) {
    return restClient
        .get()
        .uri(applicationUrl + "/applications/{id}/document", id)
        .retrieve()
        .body(byte[].class);
  }
}
