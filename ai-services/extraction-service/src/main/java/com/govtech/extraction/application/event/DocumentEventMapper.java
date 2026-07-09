package com.govtech.extraction.application.event;

import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

public interface DocumentEventMapper<T> {

    DocumentType supports();

    T toEvent(
            Document document,
            String extractedData,
            String model);
}