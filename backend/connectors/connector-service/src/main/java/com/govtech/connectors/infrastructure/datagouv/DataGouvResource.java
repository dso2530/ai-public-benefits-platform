package com.govtech.connectors.infrastructure.datagouv;

import java.net.URI;

public record DataGouvResource(

        String id,

        String title,

        URI uri,

        String format,

        long size,

        String mimeType

) {
}