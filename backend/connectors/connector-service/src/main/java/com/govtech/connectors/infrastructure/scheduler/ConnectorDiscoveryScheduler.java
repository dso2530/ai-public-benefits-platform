package com.govtech.connectors.infrastructure.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.govtech.connectors.application.usecase.DiscoverDocumentsUseCase;
import com.govtech.connectors.common.spi.DocumentSourceConnector;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConnectorDiscoveryScheduler {

    private final List<DocumentSourceConnector> connectors;

    private final DiscoverDocumentsUseCase useCase;

    @Scheduled(fixedDelayString = "${connectors.scheduler.fixed-delay:PT1H}")
    public void execute() {

        if (connectors.isEmpty()) {

            log.warn(
                    "No document source connector configured");

            return;

        }

        connectors.forEach(connector -> {

            try {

                log.info(
                        "Starting connector discovery {}",
                        connector.name());

                useCase.execute(connector);

                log.info(
                        "Connector discovery completed {}",
                        connector.name());

            } catch (Exception e) {

                log.error(
                        "Connector discovery failed {}",
                        connector.name(),
                        e);

            }

        });

    }

}