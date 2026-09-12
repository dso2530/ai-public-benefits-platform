
package com.govtech.citizen.filter;

import com.govtech.bff.security.auth.model.InternalUser;
import com.govtech.bff.security.auth.service.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InternalJwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authorization = request.getHeader("Authorization");

        if (authorization == null
                || !authorization.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        String token = authorization.substring(7).trim();

        try {

            Jws<Claims> parsed = jwtService.parseToken(token);

            Claims claims = parsed.getPayload();

            String subject = claims.getSubject();

            String email = claims.get("email", String.class);

            if (subject == null
                    || subject.isBlank()) {

                throw new IllegalStateException(
                        "Missing subject");
            }

            if (email == null
                    || email.isBlank()) {

                throw new IllegalStateException(
                        "Missing email");
            }

            InternalUser internalUser = new InternalUser(
                    subject,
                    email,
                    token,
                    null);

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    internalUser,
                    null,
                    List.of());

            SecurityContextHolder
                    .getContext()
                    .setAuthentication(authentication);

            log.info(
                    "Internal JWT authenticated: subject={}, email={}",
                    subject,
                    email);

        } catch (Exception e) {

            log.warn(
                    "Internal JWT validation failed: {}",
                    e.getMessage());

            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
