package com.govtech.bff.clients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.bff.auth.dto.ProfileDto;
import com.govtech.bff.dashboard.dto.HouseholdDto;

@Component
@RequiredArgsConstructor
public class ProfileClient {

    private final RestClient restClient;

    @Value("${services.profile.url}")
    private String profileUrl;

    public HouseholdDto getHousehold() {

         ProfileDto profileDto = restClient.get()
                .uri(profileUrl + "/api/profile/me")
                .retrieve()
                .body(ProfileDto.class);
        
        return HouseholdDto.builder()
            .city(profileDto.city())
            .housingStatus(profileDto.housingStatus())
            .children(profileDto.childrenCount())
            .singleParent(profileDto.singleParent())            
            .build();
    }
}