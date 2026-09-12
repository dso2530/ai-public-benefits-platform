
package com.govtech.citizen.interceptor;

import com.govtech.bff.security.auth.model.InternalUser;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class AuthenticationPropagationInterceptor
    implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request,
      byte[] body,
      ClientHttpRequestExecution execution)
      throws IOException {

    Authentication authentication = SecurityContextHolder
        .getContext()
        .getAuthentication();

    log.debug(
        "Outgoing request: method={}, uri={}, authentication={}",
        request.getMethod(),
        request.getURI(),
        authentication);

    if (authentication == null) {
      log.warn("No authentication available for outgoing request");
    }

    if (authentication != null) {
      log.debug(
          "Authentication type={}, principal type={}",
          authentication.getClass().getName(),
          authentication.getPrincipal() != null
              ? authentication.getPrincipal().getClass().getName()
              : "null");
    }

    if (authentication != null
        && authentication.getPrincipal() instanceof InternalUser internalUser) {

      log.debug(
          "InternalUser found: subject={}, tokenPresent={}",
          internalUser.subject(),
          internalUser.token() != null && !internalUser.token().isBlank());

      String accessToken = internalUser.token();

      if (accessToken != null && !accessToken.isBlank()) {

        request.getHeaders().setBearerAuth(accessToken);

        log.debug(
            "Propagating internal JWT: subject={}, uri={}",
            internalUser.subject(),
            request.getURI());
      }
    }

    return execution.execute(request, body);
  }
}
