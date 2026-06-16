package com.govtech.bff.auth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.govtech.bff.auth.service.JwtService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KeycloakSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

     private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        OidcUser user = (OidcUser) authentication.getPrincipal();

        String jwt = jwtService.generateToken(
                user.getSubject(),
                user.getEmail());

        Cookie cookie = new Cookie("access_token", jwt);

        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(86400);

        response.addCookie(cookie);

        response.sendRedirect(
                "http://localhost:3000/dashboard");
    }
}