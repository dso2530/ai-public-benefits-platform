package com.govtech.knowledge.infrastructure.persistence;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface KnowledgeDocumentJpaRepository
                extends JpaRepository<KnowledgeDocumentJpaEntity, Long> {

        /**
         * Recherche du document déjà indexé
         * à partir de l'identifiant du document source
         */
        Optional<KnowledgeDocumentJpaEntity> findByDocumentId(
                        Long documentId);

        /**
         * Evite une double indexation
         * si le même document est rejoué par Kafka
         */
        boolean existsByChecksum(
                        String checksum);

        /**
         * Plus adapté pour un flux OCR/Kafka :
         * même document mais nouvelle version possible
         */
        Optional<KnowledgeDocumentJpaEntity> findByDocumentIdAndChecksum(
                        Long documentId,
                        String checksum);

        /**
         * Permet de supprimer l'ancien index
         * avant réindexation
         */
        void deleteByDocumentId(
                        Long documentId);

}