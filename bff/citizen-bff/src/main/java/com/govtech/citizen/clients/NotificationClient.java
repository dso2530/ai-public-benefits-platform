package com.govtech.citizen.clients;

import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.govtech.citizen.dashboard.dto.NotificationSummaryDto;
import com.govtech.citizen.notification.dto.NotificationDto;
import com.govtech.platform.web.error.ExternalServiceUnavailableException;
import com.govtech.platform.web.rest.RestClientErrorHandler;

@Component
@RequiredArgsConstructor
public class NotificationClient {

  private static final String SERVICE_NAME = "notification-service";

  private final RestClient restClient;

  @Value("${services.notification.url}")
  private String notificationUrl;

  public NotificationSummaryDto getSummary() {

    try {

      return restClient
          .get()
          .uri(
              notificationUrl
                  + "/api/notifications/summary")
          .retrieve()
          .body(NotificationSummaryDto.class);

    } catch (RestClientResponseException ex) {

      throw RestClientErrorHandler.handle(
          ex,
          SERVICE_NAME);

    } catch (ResourceAccessException ex) {

      throw serviceUnavailable(ex);
    }
  }

  public List<NotificationDto> getNotifications() {

    try {

      return restClient
          .get()
          .uri(
              notificationUrl
                  + "/api/notifications")
          .retrieve()
          .body(
              new ParameterizedTypeReference<List<NotificationDto>>() {
              });

    } catch (RestClientResponseException ex) {

      throw RestClientErrorHandler.handle(
          ex,
          SERVICE_NAME);

    } catch (ResourceAccessException ex) {

      throw serviceUnavailable(ex);
    }
  }

  public void markAsRead(UUID id) {

    try {

      restClient
          .patch()
          .uri(
              notificationUrl
                  + "/api/notifications/{id}/read",
              id)
          .retrieve()
          .toBodilessEntity();

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
