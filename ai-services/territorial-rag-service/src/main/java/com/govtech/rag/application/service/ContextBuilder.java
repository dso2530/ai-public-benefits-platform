package com.govtech.rag.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.rag.domain.model.RetrievedChunk;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ContextBuilder {

        public String build(
                        List<RetrievedChunk> chunks) {

                log.info(
                                "Building LLM context from {} retrieved chunks",
                                chunks.size());

                StringBuilder context = new StringBuilder();

                for (RetrievedChunk chunk : chunks) {

                        log.debug(
                                        "Adding chunk documentId={}, chunkNumber={}, score={}",
                                        chunk.documentId(),
                                        chunk.chunkNumber(),
                                        chunk.score());

                        context.append("""
                                        ==================================================
                                        DOCUMENT ID : %d
                                        CHUNK       : %d
                                        SCORE       : %.4f
                                        SOURCE      : %s
                                        TERRITORY   : %s
                                        TYPE        : %s

                                        %s

                                        """
                                        .formatted(
                                                        chunk.documentId(),
                                                        chunk.chunkNumber(),
                                                        chunk.score(),
                                                        chunk.metadata().getOrDefault("source", ""),
                                                        chunk.metadata().getOrDefault("territoryCode", ""),
                                                        chunk.metadata().getOrDefault("documentType", ""),
                                                        chunk.content()));
                }

                log.info(
                                "LLM context built ({} characters)",
                                context.length());

                if (log.isDebugEnabled()) {

                        log.debug(
                                        "LLM context:\n{}",
                                        context.length() > 4000
                                                        ? context.substring(0, 4000) + "\n...[truncated]"
                                                        : context.toString());
                }

                return context.toString();
        }
}