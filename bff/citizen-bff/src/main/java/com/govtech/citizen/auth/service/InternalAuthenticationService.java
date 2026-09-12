package com.govtech.citizen.auth.service;

import com.govtech.bff.security.auth.model.InternalAuthentication;
import com.govtech.bff.security.auth.model.InternalUser;
import com.govtech.bff.security.auth.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InternalAuthenticationService {

        private final JwtService jwtService;

        public InternalAuthentication authenticate(
                        String subject,
                        String email,
                        String fullName) {

                InternalAuthentication internalAccessToken = jwtService.generateToken(
                                subject,
                                email);

                InternalUser internalUser = new InternalUser(
                                subject,
                                email,
                                internalAccessToken.accessToken(),
                                fullName);

                Authentication authentication = new UsernamePasswordAuthenticationToken(
                                internalUser,
                                null,
                                List.of());

                SecurityContextHolder
                                .getContext()
                                .setAuthentication(authentication);

                log.info(
                                "Internal user authenticated: subject={}, email={}",
                                subject,
                                maskEmail(email));

                return internalAccessToken;
        }

        public void clear() {

                SecurityContextHolder.clearContext();

                log.info(
                                "Internal security context cleared");
        }

        private String maskEmail(
                        String email) {

                int at = email.indexOf('@');

                if (at <= 1) {
                        return "***";
                }

                return email.charAt(0)
                                + "***"
                                + email.substring(at);
        }
}