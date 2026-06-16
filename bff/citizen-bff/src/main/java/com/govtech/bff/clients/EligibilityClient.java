package com.govtech.bff.clients;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.bff.dashboard.dto.BenefitsDto;

@Component
@RequiredArgsConstructor
public class EligibilityClient {

    private final RestClient restClient;

    @Value("${services.eligibility.url}")
    private String eligibilityUrl;

    public BenefitsDto getSummary() {

        BenefitsDto benefits =
                new BenefitsDto(
                        7,
                        2,
                        4280
                );

        return benefits;/*restClient.get()
                .uri(eligibilityUrl + "/api/eligibility/summary")
                .retrieve()
                .body(BenefitsDto.class);*/
    }
}