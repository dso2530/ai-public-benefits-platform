package com.govtech.bff.clients;

import com.govtech.bff.dashboard.dto.NotificationSummaryDto;
import com.govtech.bff.notification.dto.NotificationDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class NotificationClient {

  private final RestClient restClient;

  @Value("${services.notification.url}")
  private String notificationUrl;

  public NotificationSummaryDto getSummary() {

    return restClient
        .get()
        .uri(notificationUrl + "/api/notifications/summary")
        .retrieve()
        .body(NotificationSummaryDto.class);
  }

  public List<NotificationDto> getNotifications() {
    return restClient
        .get()
        .uri(notificationUrl + "/api/notifications")
        .retrieve()
        .body(new ParameterizedTypeReference<List<NotificationDto>>() {});
  }

  public void markAsRead(UUID id) {
    restClient
        .patch()
        .uri(notificationUrl + "/api/notifications/{id}/read", id)
        .retrieve()
        .toBodilessEntity();
  }
}
