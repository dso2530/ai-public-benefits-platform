package com.govtech.connectors.common.downloader;

import java.io.IOException;

import org.apache.tika.Tika;
import org.springframework.stereotype.Component;

@Component
public class TikaContentTypeDetector
        implements ContentTypeDetector {

    private final Tika tika = new Tika();

    @Override
    public String detect(
            byte[] content)
            throws IOException {

        return tika.detect(content);

    }

}