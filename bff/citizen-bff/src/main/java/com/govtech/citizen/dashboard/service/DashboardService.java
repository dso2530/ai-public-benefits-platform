package com.govtech.citizen.dashboard.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.govtech.citizen.clients.ApplicationClient;
import com.govtech.citizen.clients.DocumentClient;
import com.govtech.citizen.clients.EligibilityClient;
import com.govtech.citizen.clients.NotificationClient;
import com.govtech.citizen.clients.ProfileClient;
import com.govtech.citizen.dashboard.dto.*;

@Service
@RequiredArgsConstructor
public class DashboardService {

  private final ProfileClient profileClient;
  private final EligibilityClient eligibilityClient;
  private final NotificationClient notificationClient;
  private final DocumentClient documentClient;
  private final ApplicationClient applicationClient;

  public DashboardResponse getDashboard() {

    return DashboardResponse.builder()
        .household(profileClient.getHousehold())
        .benefits(eligibilityClient.getSummary())
        .notifications(
            new NotificationSummaryDto(
                2,
                2))
        .documents(documentClient.getSummary())
        .applications(applicationClient.getSummary())
        .build();
  }
}
