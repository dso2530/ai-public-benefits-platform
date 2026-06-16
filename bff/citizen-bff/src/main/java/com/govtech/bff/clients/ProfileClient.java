package com.govtech.bff.clients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.bff.dashboard.dto.HouseholdDto;

@Component
@RequiredArgsConstructor
public class ProfileClient {

    private final RestClient restClient;

    @Value("${services.profile.url}")
    private String profileUrl;

    public HouseholdDto getHousehold() {

        HouseholdDto household =
                new HouseholdDto(
                        "Roubaix",
                        "TENANT",
                        2,
                        true
                );

        return household;/*restClient.get()
                .uri(profileUrl + "/api/profile")
                .retrieve()
                .body(HouseholdDto.class)*/ 
    }
}