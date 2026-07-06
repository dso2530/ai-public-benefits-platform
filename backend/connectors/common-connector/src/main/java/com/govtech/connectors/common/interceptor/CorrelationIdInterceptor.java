package com.govtech.connectors.common.interceptor;

import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CorrelationIdInterceptor implements ClientHttpRequestInterceptor {

  private final Tracer tracer;

  @Override
  public org.springframework.http.client.ClientHttpResponse intercept(
      org.springframework.http.HttpRequest request,
      byte[] body,
      org.springframework.http.client.ClientHttpRequestExecution execution)
      throws java.io.IOException {

    var span = tracer.currentSpan();

    if (span != null) {

      request.getHeaders().add("X-Correlation-Id", span.context().traceId());

      request.getHeaders().add("X-Trace-Id", span.context().traceId());

      request.getHeaders().add("X-Span-Id", span.context().spanId());
    }

    return execution.execute(request, body);
  }
}
