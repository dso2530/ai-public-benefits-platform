package com.govtech.bff.dashboard.service;

import com.govtech.bff.clients.EligibilityClient;
import com.govtech.bff.clients.NotificationClient;
import com.govtech.bff.clients.ProfileClient;
import com.govtech.bff.dashboard.dto.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {


      private final ProfileClient profileClient;
      private final EligibilityClient eligibilityClient;
      private final NotificationClient notificationClient;

      public DashboardResponse getDashboard() {

        return DashboardResponse.builder()
                .household(profileClient.getHousehold())
                .benefits(eligibilityClient.getSummary())
                .notifications(notificationClient.getSummary())
                .build();
    }

    /*public DashboardResponse getDashboard() {

        HouseholdDto household =
                new HouseholdDto(
                        "Roubaix",
                        "TENANT",
                        2,
                        true
                );

        BenefitsDto benefits =
                new BenefitsDto(
                        7,
                        2,
                        4280
                );

        NotificationsDto notifications =
                new NotificationsDto(
                        3
                );

        return new DashboardResponse(
                household,
                benefits,
                notifications
        );
    }*/
}