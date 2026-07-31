package com.govtech.connectors.domain.port;

import java.util.Optional;

import com.govtech.connectors.common.model.ConnectorDocument;

public interface DocumentRegistry {

    boolean exists(String checksum);

    Optional<ConnectorDocument> findByChecksum(String checksum);

    void save(ConnectorDocument document);

}