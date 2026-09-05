package com.govtech.knowledge.application.service;

import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.govtech.knowledge.infrastructure.persistence.KnowledgeDocumentJpaEntity;
import com.govtech.knowledge.infrastructure.persistence.KnowledgeDocumentJpaRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KnowledgeDocumentService {

    private final KnowledgeDocumentJpaRepository repository;

    @Transactional
    public KnowledgeDocumentJpaEntity getOrCreate(

            Long documentId,

            String source,

            String territoryCode,

            String documentType) {

        return repository.findByDocumentId(documentId)

                .orElseGet(() ->

                repository.save(

                        KnowledgeDocumentJpaEntity.builder()

                                .documentId(documentId)

                                .source(source)

                                .territoryCode(territoryCode)

                                .documentType(documentType)

                                .indexedAt(Instant.now())

                                .createdAt(Instant.now())

                                .build()));

    }

}