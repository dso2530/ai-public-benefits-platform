package com.govtech.connectors.infrastructure.caf;

import java.util.List;

public record CafDocumentSearchResponse(

                List<Document> data

) {

        public record Document(

                        String id,

                        String title,

                        String description,

                        String url,

                        String format,

                        Long filesize,

                        String mime

        ) {
        }
}