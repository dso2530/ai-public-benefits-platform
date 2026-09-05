package com.govtech.citizen.clients;

import com.govtech.bff.security.auth.dto.ProfileDto;
import com.govtech.citizen.dashboard.dto.HouseholdDto;
import com.govtech.platform.web.error.ExternalServiceUnavailableException;
import com.govtech.platform.web.rest.RestClientErrorHandler;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
@RequiredArgsConstructor
public class ProfileClient {

  private static final String SERVICE_NAME = "profile-service";

  private final RestClient restClient;

  @Value("${services.profile.url}")
  private String profileUrl;

  public HouseholdDto getHousehold() {

    try {

      ProfileDto profileDto = restClient
          .get()
          .uri(profileUrl + "/api/profile/me")
          .retrieve()
          .body(ProfileDto.class);

      return HouseholdDto.builder()
          .city(profileDto.city())
          .housingStatus(profileDto.housingStatus())
          .children(profileDto.childrenCount())
          .singleParent(profileDto.singleParent())
          .build();

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
