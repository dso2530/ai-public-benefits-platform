package com.govtech.extraction.application.event;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.govtech.extraction.domain.model.Document;
import com.govtech.extraction.domain.model.DocumentType;

@Component
public class DocumentEventFactory {

    private final Map<DocumentType, DocumentEventMapper<?>> mappers;

    public DocumentEventFactory(List<DocumentEventMapper<?>> mappers) {
        this.mappers = mappers.stream()
                .collect(Collectors.toUnmodifiableMap(
                        DocumentEventMapper::supports,
                        Function.identity()));
    }

    public Object create(
            Document document,
            String extractedData,
            String model) {

        DocumentEventMapper<?> mapper = mappers.get(document.type());

        if (mapper == null) {
            throw new IllegalArgumentException(
                    "No DocumentEventMapper registered for " + document.type());
        }

        return mapper.toEvent(document, extractedData, model);
    }
}