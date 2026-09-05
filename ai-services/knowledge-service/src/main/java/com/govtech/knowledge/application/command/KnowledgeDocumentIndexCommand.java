package com.govtech.knowledge.application.command;

import java.time.Instant;

public record KnowledgeDocumentIndexCommand(

        Long documentId,       

        String source,

        String territoryCode,

        String documentType,

        String bucket,

        String objectKey,

        String contentType,

        String text,

        String checksum,

        Instant processedAt

) {

}