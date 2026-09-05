package com.govtech.apply.application.client;

import java.util.List;

import com.govtech.apply.application.dto.StoredDocument;

public interface DocumentClient {

    public List<StoredDocument> getDocuments(String subject);

}
