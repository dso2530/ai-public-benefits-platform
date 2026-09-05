package com.govtech.platform.messaging.event;

import lombok.Builder;

@Builder
public record EventContext(
        String correlationId,
        String causationId) {

    public static EventContext from(
            String correlationId,
            String causationId) {

        return new EventContext(
                correlationId,
                causationId);
    }
}