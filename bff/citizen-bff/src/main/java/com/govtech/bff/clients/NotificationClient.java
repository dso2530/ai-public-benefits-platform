package com.govtech.bff.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.bff.dashboard.dto.BenefitsDto;
import com.govtech.bff.dashboard.dto.NotificationsDto;

@Component
@RequiredArgsConstructor
public class NotificationClient {

    private final RestClient restClient;

    @Value("${services.notification.url}")
    private String notificationUrl;

    public NotificationsDto getSummary() {

         NotificationsDto notifications =
                new NotificationsDto(
                        3
                );
        

        return notifications; /*restClient.get()
                .uri(notificationUrl + "/api/notifications/summary")
                .retrieve()
                .body(NotificationsDto.class);*/
    }
}