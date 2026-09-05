package com.govtech.bff.security.auth.handler;

import com.govtech.bff.security.auth.model.InternalAuthentication;
import com.govtech.bff.security.auth.model.InternalUser;
import com.govtech.bff.security.auth.service.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KeycloakSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {

    OidcUser user = (OidcUser) authentication.getPrincipal();

    InternalAuthentication jwt = jwtService.generateToken(user.getSubject(), user.getEmail());

    InternalUser internalUser = new InternalUser(user.getSubject(), user.getEmail(), jwt.accessToken(),
        user.getFullName());

    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(internalUser, null, List.of());

    SecurityContextHolder.getContext().setAuthentication(auth);

    Cookie cookie = new Cookie("access_token", jwt.accessToken());

    cookie.setHttpOnly(true);
    cookie.setPath("/");
    cookie.setMaxAge(86400);
    cookie.setSecure(true);
    response.addCookie(cookie);

    response.sendRedirect("http://localhost:3000/dashboard");
  }
}
