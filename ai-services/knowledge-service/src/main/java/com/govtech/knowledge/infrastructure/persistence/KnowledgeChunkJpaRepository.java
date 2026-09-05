package com.govtech.knowledge.infrastructure.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface KnowledgeChunkJpaRepository
                extends JpaRepository<KnowledgeChunkJpaEntity, Long> {

        @Query("""
                        SELECT c
                        FROM KnowledgeChunkJpaEntity c
                        WHERE c.document.id = :knowledgeDocumentId
                        ORDER BY c.chunkNumber
                        """)
        List<KnowledgeChunkJpaEntity> findChunks(
                        @Param("knowledgeDocumentId") Long knowledgeDocumentId);

        @Modifying
        @Query("""
                        DELETE FROM KnowledgeChunkJpaEntity c
                        WHERE c.document.id = :knowledgeDocumentId
                        """)
        void deleteChunks(
                        @Param("knowledgeDocumentId") Long knowledgeDocumentId);

        
        

}