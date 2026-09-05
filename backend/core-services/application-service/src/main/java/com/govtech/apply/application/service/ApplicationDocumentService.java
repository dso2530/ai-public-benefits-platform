package com.govtech.apply.application.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.govtech.apply.application.client.DocumentClient;
import com.govtech.apply.application.dto.DocumentAnalysis;
import com.govtech.apply.application.dto.RequiredDocument;
import com.govtech.apply.application.dto.StoredDocument;
import com.govtech.shared.model.DocumentType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationDocumentService {

    private final RulesService rulesService;
    private final DocumentClient documentClient;

    public DocumentAnalysis analyze(
            String aidCode,
            String subject) {

        List<RequiredDocument> required = rulesService.requiredDocuments(aidCode);

        List<StoredDocument> stored = documentClient.getDocuments(subject);

        Map<DocumentType, StoredDocument> available = stored.stream()
                .collect(Collectors.toMap(
                        StoredDocument::documentType,
                        Function.identity(),
                        (existing, replacement) -> replacement));

        List<DocumentType> missing = required.stream()
                .map(RequiredDocument::type)
                .filter(type -> !available.containsKey(type))
                .toList();

        return new DocumentAnalysis(
                available,
                missing);
    }
}