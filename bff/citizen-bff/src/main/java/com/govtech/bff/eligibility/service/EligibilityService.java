package com.govtech.bff.eligibility.service;

import com.govtech.bff.application.dto.ApplicationDto;
import com.govtech.bff.clients.ApplicationClient;
import com.govtech.bff.clients.EligibilityClient;
import com.govtech.bff.eligibility.dto.EligibilityDto;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EligibilityService {

  private final EligibilityClient eligibilityClient;
  private final ApplicationClient applicationClient;

  public List<EligibilityDto> getEligibilities() {

    List<EligibilityDto> eligibilities = eligibilityClient.eligibilities();
    List<ApplicationDto> applications = applicationClient.retrieveAlls();

    Map<String, UUID> applicationIds =
        applications.stream()
            .collect(
                Collectors.toMap(
                    ApplicationDto::aidCode, ApplicationDto::id, (first, second) -> first));

    return eligibilities.stream()
        .map(
            e ->
                new EligibilityDto(
                    e.aidCode(),
                    e.aidName(),
                    e.description(),
                    e.category(),
                    e.status(),
                    e.estimatedAmount(),
                    e.estimatedAmountLabel(),
                    e.reason(),
                    e.canApply(),
                    e.actionLabel(),
                    e.icon(),
                    applicationIds.get(e.aidCode().replace("-", "_").toUpperCase())))
        .toList();
  }
}
