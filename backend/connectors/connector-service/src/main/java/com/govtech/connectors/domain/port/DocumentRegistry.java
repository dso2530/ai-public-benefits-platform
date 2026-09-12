package com.govtech.connectors.domain.port;

import java.util.Optional;

import com.govtech.connectors.common.model.RegisteredDocument;

public interface DocumentRegistry {

    boolean existsByExternalId(
            String externalId);

    boolean existsByChecksum(
            String checksum);

    void save(
            String externalId,
            String checksum,
            String source);

    Optional<RegisteredDocument> findByChecksum(
            String checksum);

}