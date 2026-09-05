package com.govtech.documentextra.domain.port;

import java.io.InputStream;

import com.govtech.documentextra.domain.model.ExtractionResult;

public interface DocumentExtractor {

    boolean supports(String contentType);

    ExtractionResult extract(
            InputStream inputStream);

}