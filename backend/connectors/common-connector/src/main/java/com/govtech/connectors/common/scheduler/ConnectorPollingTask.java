package com.govtech.connectors.common.scheduler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class ConnectorPollingTask {

    public void execute() {

        try {

            poll();

        } catch (Exception e) {

            log.error(
                    "Connector polling failed",
                    e);

        }

    }

    protected abstract void poll();

}