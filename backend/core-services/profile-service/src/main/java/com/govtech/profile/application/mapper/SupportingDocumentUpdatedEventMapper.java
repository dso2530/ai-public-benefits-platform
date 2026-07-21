package com.govtech.profile.application.mapper;

import com.govtech.events.common.DocumentBaseEvent;
import com.govtech.events.supportingdocument.SupportingDocumentUpdatedEvent;

public final class SupportingDocumentUpdatedEventMapper {

        private SupportingDocumentUpdatedEventMapper() {
        }

        public static SupportingDocumentUpdatedEvent toEvent(
                        DocumentBaseEvent metadata) {

                return SupportingDocumentUpdatedEvent.newBuilder()
                                .setMetadata(metadata)
                                .build();
        }
}