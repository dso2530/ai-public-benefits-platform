package com.govtech.connectors.common.interceptor;

import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ObservationInterceptor implements ClientHttpRequestInterceptor {

  private final ObservationRegistry observationRegistry;

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

    return Observation.createNotStarted("connector.http", observationRegistry)
        .lowCardinalityKeyValue("method", request.getMethod().name())
        .lowCardinalityKeyValue("uri", request.getURI().getPath())
        .observeChecked(() -> execution.execute(request, body));
  }
}
