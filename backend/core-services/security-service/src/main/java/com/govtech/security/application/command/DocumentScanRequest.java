package com.govtech.security.application.command;

import java.util.Map;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.shared.model.DocumentOrigin;
import com.govtech.shared.model.DocumentType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DocumentScanRequest {

    private Long documentId;

    private byte[] content;

    private String sha256;

    private DocumentOrigin origin;

    private String bucket;

    private String objectKey;

    private String contentType;

    private String fileName;

    private DocumentType documentType;

    private Map<String, String> metadata;

    private EventContext eventContext;
}