package com.govtech.bff.clients;

import com.govtech.bff.dashboard.dto.NotificationSummaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
}
