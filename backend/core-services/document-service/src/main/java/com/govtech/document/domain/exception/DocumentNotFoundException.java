package com.govtech.document.domain.exception;

public class DocumentNotFoundException extends RuntimeException {

    private final Long documentId;

    public DocumentNotFoundException(Long documentId) {

        super(
                "Document not found with id: " + documentId);

        this.documentId = documentId;
    }

    public Long getDocumentId() {

        return documentId;
    }

}