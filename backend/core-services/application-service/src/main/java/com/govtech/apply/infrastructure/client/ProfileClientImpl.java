package com.govtech.apply.infrastructure.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import com.govtech.apply.application.client.ProfileClient;
import com.govtech.apply.application.client.dto.ProfileContractDto;

@Component
public class ProfileClientImpl implements ProfileClient {

        private final RestClient restClient;

        public ProfileClientImpl(
                        @Value("${clients.profile.base-url}") String baseUrl) {

                this.restClient = RestClient.builder()
                                .baseUrl(baseUrl)
                                .requestFactory(
                                                new HttpComponentsClientHttpRequestFactory())
                                .build();
        }

        @Override
        public ProfileContractDto getProfile(String subject) {

                return restClient.get()
                                .uri("/internal/profiles/{subject}", subject)
                                .retrieve()
                                .body(ProfileContractDto.class);
        }
}