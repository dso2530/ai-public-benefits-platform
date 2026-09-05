package com.govtech.connectors.infrastructure.caf;

import java.util.List;

import org.springframework.stereotype.Component;

import com.govtech.connectors.common.model.ConnectorDocument;
import com.govtech.connectors.common.spi.DocumentSourceConnector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CafConnector
        implements DocumentSourceConnector {

    private final CafClient client;

    private final CafDocumentResourceMapper mapper;

    @Override
    public String name() {
        return "caf";
    }

    @Override
    public List<ConnectorDocument> discover() {

        log.info("Discovering documents from CAF");

        return client.documents()
                .stream()
                .map(mapper::map)
                .toList();
    }
}