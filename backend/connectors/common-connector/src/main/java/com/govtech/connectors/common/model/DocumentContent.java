package com.govtech.connectors.common.model;

public record DocumentContent(

        byte[] content,

        String fileName,

        String contentType,

        long size,

        String checksum,

        String bucket,

        String objectKey

) {
}