package com.govtech.document.application.service;

import org.springframework.stereotype.Service;

import com.govtech.platform.storage.service.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentStorageMoveService {

    private final StorageService storageService;

    public void moveIfNecessary(
            Long documentId,
            String sourceBucket,
            String sourceObjectKey,
            String destinationBucket,
            String destinationObjectKey) {

        boolean sourceExists = storageService.exists(
                sourceBucket,
                sourceObjectKey);

        if (sourceExists) {

            storageService.move(
                    sourceBucket,
                    sourceObjectKey,
                    destinationBucket,
                    destinationObjectKey);

            log.info(
                    "Document moved documentId={} sourceBucket={} source={} destinationBucket={} destination={}",
                    documentId,
                    sourceBucket,
                    sourceObjectKey,
                    destinationBucket,
                    destinationObjectKey);

            return;
        }

        boolean destinationExists = storageService.exists(
                destinationBucket,
                destinationObjectKey);

        if (destinationExists) {

            log.info(
                    "Document already moved, treating operation as idempotent documentId={} destination={}",
                    documentId,
                    destinationObjectKey);

            return;
        }

        throw new IllegalStateException(
                "Document not found in source or destination storage "
                        + "documentId=" + documentId
                        + " sourceBucket=" + sourceBucket
                        + " source=" + sourceObjectKey
                        + " destinationBucket=" + destinationBucket
                        + " destination=" + destinationObjectKey);
    }
}