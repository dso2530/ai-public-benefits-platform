/*package com.govtech.bff.auth.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.govtech.bff.auth.service.JwtService;
import com.govtech.bff.auth.service.UserProvisioningService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class FranceConnectSuccessHandler
        extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserProvisioningService provisioningService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException {

        OidcUser user =
                (OidcUser) authentication.getPrincipal();

        provisioningService.provisionUser(user);

        String jwt =
                jwtService.generateToken(
                        user.getSubject(),user.getEmail());

        response.sendRedirect(
                "http://localhost:3000/auth/callback?token="
                        + jwt);
    }
}
*/