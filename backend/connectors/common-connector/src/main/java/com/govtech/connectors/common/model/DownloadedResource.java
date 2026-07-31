package com.govtech.connectors.common.model;

public record DownloadedResource(

        String fileName,

        String contentType,

        long size,

        byte[] content

) {
}