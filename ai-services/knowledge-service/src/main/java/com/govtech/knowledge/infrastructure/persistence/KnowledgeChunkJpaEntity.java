package com.govtech.knowledge.infrastructure.persistence;

import java.time.Instant;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "knowledge_chunk", indexes = {
        @Index(name = "idx_knowledge_chunk_document", columnList = "knowledge_document_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeChunkJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "knowledge_document_id", nullable = false)
    private KnowledgeDocumentJpaEntity document;

    @Column(name = "chunk_number", nullable = false)
    private Integer chunkNumber;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * Metadata JSON du chunk.
     *
     * Exemple:
     * {
     * "source":"DATAGOUV",
     * "territoryCode":"59",
     * "documentType":"CSV"
     * }
     */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = false)
    private Map<String, String> metadata;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

}