package com.govtech.platform.web.correlation;

import org.slf4j.MDC;

import java.util.UUID;

public final class CorrelationId {

    public static final String MDC_KEY = "correlationId";

    private CorrelationId() {
    }

    public static String getOrCreate() {

        String correlationId = MDC.get(MDC_KEY);

        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
            MDC.put(MDC_KEY, correlationId);
        }

        return correlationId;
    }

    public static String get() {
        return MDC.get(MDC_KEY);
    }
}
