package com.govtech.bff.clients;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.bff.dashboard.dto.EligibilitySummaryDto;
import com.govtech.bff.eligibility.dto.EligibilityDto;

@Component
@RequiredArgsConstructor
public class EligibilityClient {

    private final RestClient restClient;

    @Value("${services.eligibility.url}")
    private String eligibilityUrl;

    public EligibilitySummaryDto getSummary() {      

        return restClient.get()
                .uri(eligibilityUrl + "/api/eligibility/summary")
                .retrieve()
                .body(EligibilitySummaryDto.class);
    }

    public List<EligibilityDto>  eligibilities()  {
        return restClient.get()
                .uri(eligibilityUrl + "/api/eligibility")
                .retrieve()
                 .body(new ParameterizedTypeReference<List<EligibilityDto>>() {}); 
    }
}