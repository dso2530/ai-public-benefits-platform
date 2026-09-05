
package com.govtech.document.application.command;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import com.govtech.shared.model.ConnectorType;
import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

import lombok.Builder;

@Builder
public record RegisterDocumentCommand(

        String subject,

        UUID applicationId,

        String name,

        String fileName,

        DocumentType documentType,

        String contentType,

        Long fileSize,

        String sha256,

        Instant uploadedAt,

        DocumentOrigin origin,

        String source,

        String connectorName,

        ConnectorType connectorType,

        String sourceUri,

        String territoryCode,

        String bucket,

        String objectKey,

        Map<String, String> metadata

) {
}