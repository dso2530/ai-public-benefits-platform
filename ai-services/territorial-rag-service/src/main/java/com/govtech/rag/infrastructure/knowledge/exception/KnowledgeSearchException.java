package com.govtech.rag.infrastructure.knowledge.exception;

/**
 * KnowledgeSearchException
 */
public class KnowledgeSearchException extends RuntimeException {

    public KnowledgeSearchException(String message) {
        super(message);
    }

    public KnowledgeSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}