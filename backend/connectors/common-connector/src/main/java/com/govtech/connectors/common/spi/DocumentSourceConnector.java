package com.govtech.connectors.common.spi;

import java.util.List;

import com.govtech.connectors.common.model.ConnectorDocument;

public interface DocumentSourceConnector
        extends Connector {

    /**
     * Découvre les nouveaux documents disponibles.
     */
    List<ConnectorDocument> discover();

}