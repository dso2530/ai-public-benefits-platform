package com.govtech.connectors.common.model;

import java.net.URI;
import java.time.Instant;
import java.util.List;


public record DocumentPage(

        URI source,

        String title,

        String content,

        String contentType,

        Instant fetchedAt,

        List<URI> links

) {


    public DocumentPage {

        if (fetchedAt == null) {
            fetchedAt = Instant.now();
        }

        if (links == null) {
            links = List.of();
        }

    }

}