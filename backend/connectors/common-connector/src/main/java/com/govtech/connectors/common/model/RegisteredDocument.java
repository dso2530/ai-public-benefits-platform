package com.govtech.connectors.common.model;

public record RegisteredDocument(

        String externalId,

        String checksum,

        String source

) {
}