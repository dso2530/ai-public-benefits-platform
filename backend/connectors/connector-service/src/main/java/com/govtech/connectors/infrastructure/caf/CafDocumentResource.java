package com.govtech.connectors.infrastructure.caf;

import java.net.URI;

public record CafDocumentResource(

                String id,

                String title,

                URI uri,

                String format,

                long size,

                String mimeType

) {
}