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

import com.govtech.citizen.application.dto.ApplicationDto;
import com.govtech.citizen.application.dto.ApplicationStatus;
import com.govtech.citizen.dashboard.dto.ApplicationsSummaryDto;
import com.govtech.platform.web.error.ExternalServiceUnavailableException;
import com.govtech.platform.web.rest.RestClientErrorHandler;

@Component
@RequiredArgsConstructor
public class ApplicationClient {

    private final RestClient restClient;

    @Value("${services.application.url}")
    private String applicationUrl;

    private static final String SERVICE_NAME = "application-service";

    public ApplicationsSummaryDto getSummary() {

        List<ApplicationDto> applications = retrieveAlls();

        return ApplicationsSummaryDto.builder()
                .total(applications.size())
                .generated(
                        count(
                                applications,
                                ApplicationStatus.READY_TO_COMPLETE
                        )
                )
                .readyToSubmit(
                        count(
                                applications,
                                ApplicationStatus.READY_TO_SUBMIT
                        )
                )
                .submitted(
                        count(
                                applications,
                                ApplicationStatus.SUBMITTED
                        )
                )
                .accepted(
                        count(
                                applications,
                                ApplicationStatus.ACCEPTED
                        )
                )
                .rejected(
                        count(
                                applications,
                                ApplicationStatus.REJECTED
                        )
                )
                .build();
    }

    private int count(
            List<ApplicationDto> applications,
            ApplicationStatus status) {

        return (int) applications.stream()
                .filter(application -> application.status() == status)
                .count();
    }

    public List<ApplicationDto> retrieveAlls() {

        try {

            return restClient
                    .get()
                    .uri(applicationUrl + "/api/applications")
                    .retrieve()
                    .body(
                            new ParameterizedTypeReference<List<ApplicationDto>>() {
                            }
                    );

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    public ApplicationDto findById(UUID id) {

        try {

            return restClient
                    .get()
                    .uri(
                            applicationUrl + "/api/applications/{id}",
                            id
                    )
                    .retrieve()
                    .body(ApplicationDto.class);

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    public void submit(UUID id) {

        try {

            restClient
                    .post()
                    .uri(
                            applicationUrl
                                    + "/api/applications/{id}/submit",
                            id
                    )
                    .retrieve()
                    .toBodilessEntity();

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    public byte[] download(UUID id) {

        try {

            return restClient
                    .get()
                    .uri(
                            applicationUrl
                                    + "/api/applications/{id}/document",
                            id
                    )
                    .retrieve()
                    .body(byte[].class);

        } catch (RestClientResponseException ex) {

            throw RestClientErrorHandler.handle(
                    ex,
                    SERVICE_NAME
            );

        } catch (ResourceAccessException ex) {

            throw serviceUnavailable(ex);
        }
    }

    private ExternalServiceUnavailableException serviceUnavailable(
            ResourceAccessException ex) {

        return new ExternalServiceUnavailableException(
                "Le service " + SERVICE_NAME
                        + " est indisponible.",
                ex
        );
    }
}
