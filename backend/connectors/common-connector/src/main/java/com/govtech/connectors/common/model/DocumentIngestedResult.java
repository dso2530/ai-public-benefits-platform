package com.govtech.connectors.common.model;

public record DocumentIngestedResult(

        String externalId,

        String bucket,

        String objectKey,

        String checksum,

        String contentType,

        long size

) {
}