package com.govtech.connectors.infrastructure.datagouv;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataGouvClient {

        private final RestClient connectorRestClient;

        private final DataGouvProperties properties;

        public DataGouvDatasetResponse search() {

                return connectorRestClient.get()

                                .uri(uriBuilder -> uriBuilder
                                                .scheme("https")
                                                .host("www.data.gouv.fr")
                                                .path("/api/1/datasets/")
                                                .queryParam("q", properties.query())
                                                .queryParam("page_size", properties.limit())
                                                .build())

                                .retrieve()

                                .body(DataGouvDatasetResponse.class);

        }

}