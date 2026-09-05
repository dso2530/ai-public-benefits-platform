package com.govtech.knowledge.domain.model;

import java.time.Instant;

public record KnowledgeDocument(

        Long documentId,

        String source,

        String territoryCode,

        String documentType,

        String title,

        String checksum,

        Instant indexedAt

) {

}