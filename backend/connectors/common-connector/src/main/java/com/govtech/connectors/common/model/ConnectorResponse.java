package com.govtech.connectors.common.model;

import java.time.Instant;
import java.util.Map;


public record ConnectorResponse<T>(

        boolean success,

        T data,

        String source,

        Instant timestamp,

        Map<String,String> metadata

) {


    public ConnectorResponse {

        if (timestamp == null) {

            timestamp = Instant.now();

        }

        if (metadata == null) {

            metadata = Map.of();

        }

    }


    public static <T> ConnectorResponse<T> success(
            String source,
            T data) {

        return new ConnectorResponse<>(
                true,
                data,
                source,
                Instant.now(),
                Map.of());

    }


    public static <T> ConnectorResponse<T> failure(
            String source,
            String message) {

        return new ConnectorResponse<>(
                false,
                null,
                source,
                Instant.now(),
                Map.of(
                    "error",
                    message));

    }

}