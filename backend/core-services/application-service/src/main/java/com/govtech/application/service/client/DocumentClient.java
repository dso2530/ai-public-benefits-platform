package com.govtech.application.service.client;

import java.util.List;

import com.govtech.application.domain.model.StoredDocument;

public interface DocumentClient {

    public List<StoredDocument> getDocuments(String subject);

}
