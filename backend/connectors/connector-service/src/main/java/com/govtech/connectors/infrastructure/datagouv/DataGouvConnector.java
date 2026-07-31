package com.govtech.connectors.infrastructure.datagouv;


import java.util.List;

import org.springframework.stereotype.Component;

import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.common.spi.DocumentSourceConnector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class DataGouvConnector
        implements DocumentSourceConnector {


    private final DataGouvClient client;

    private final ResourceExtractor extractor;

    private final DataGouvResourceMapper mapper;



    @Override
    public String name() {

        return "datagouv";

    }



    @Override
    public List<ConnectorDocument> discover() {


        log.info(
            "Discovering documents from data.gouv.fr");


        DataGouvDatasetResponse response =
                client.search();



        if (response == null ||
            response.data() == null) {

            return List.of();

        }



        return response.data()

                .stream()

                .flatMap(dataset ->

                    extractor.extract(dataset)
                            .stream()

                )

                .map(mapper::map)

                .toList();

    }

}