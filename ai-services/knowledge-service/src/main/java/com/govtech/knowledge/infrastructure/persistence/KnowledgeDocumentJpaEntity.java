package com.govtech.knowledge.infrastructure.persistence;


import java.time.Instant;

import jakarta.persistence.*;

import lombok.*;


@Entity
@Table(
    name = "knowledge_document",
    indexes = {

        @Index(
            name = "idx_knowledge_document_document",
            columnList = "document_id"),

        @Index(
            name = "idx_knowledge_document_source",
            columnList = "source"),

        @Index(
            name = "idx_knowledge_document_territory",
            columnList = "territory_code")

    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeDocumentJpaEntity {


    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY)
    private Long id;



    @Column(
        name = "document_id",
        nullable = false)
    private Long documentId;



    @Column(
        nullable = false,
        length = 100)
    private String source;



    @Column(
        name = "territory_code",
        length = 20)
    private String territoryCode;



    @Column(
        name = "document_type",
        nullable = false,
        length = 100)
    private String documentType;



    @Column(
        length = 500)
    private String title;



    @Column(
        length = 64)
    private String checksum;



    @Column(
        name = "indexed_at",
        nullable = false)
    private Instant indexedAt;



    @Column(
        name = "created_at",
        nullable = false)
    private Instant createdAt;


}