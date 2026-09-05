package com.govtech.platform.web.correlation;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CorrelationIdInterceptor implements ClientHttpRequestInterceptor {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    private static final String TRACE_ID_HEADER = "X-Trace-Id";
    private static final String SPAN_ID_HEADER = "X-Span-Id";

    private final Tracer tracer;

    @Override
    public ClientHttpResponse intercept(
            HttpRequest request,
            byte[] body,
            ClientHttpRequestExecution execution)
            throws IOException {

        // Business correlation ID
        String correlationId = CorrelationId.get();

        if (correlationId != null && !correlationId.isBlank()) {
            request.getHeaders().set(
                    CORRELATION_ID_HEADER,
                    correlationId);
        }

        // Distributed tracing
        Span span = tracer.currentSpan();

        if (span != null) {
            var context = span.context();

            request.getHeaders().set(
                    TRACE_ID_HEADER,
                    context.traceId());

            request.getHeaders().set(
                    SPAN_ID_HEADER,
                    context.spanId());
        }

        return execution.execute(request, body);
    }
}