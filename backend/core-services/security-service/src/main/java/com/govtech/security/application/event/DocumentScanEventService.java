package com.govtech.security.application.event;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.govtech.platform.messaging.event.EventContext;
import com.govtech.security.application.service.SecurityOutboxService;
import com.govtech.security.domain.model.SecurityScan;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DocumentScanEventService {

        private final SecurityOutboxService outboxService;
        private final DocumentScanEventFactory documentEventFactory;

        public void publishScanCompleted(
                        SecurityScan scan,
                        Map<String, String> metadata,
                        EventContext eventContext) {

                outboxService.publish(
                                scan,
                                eventContext,
                                (securityScan, context) -> documentEventFactory.buildScanCompleted(
                                                securityScan,
                                                metadata,
                                                context),
                                "DocumentScanCompleted",
                                "document.scan.completed");
        }
}