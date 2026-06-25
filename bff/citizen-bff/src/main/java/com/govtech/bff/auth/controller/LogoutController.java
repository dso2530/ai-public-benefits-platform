package com.govtech.bff.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/auth")
public class LogoutController {

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(
            HttpServletResponse response) {

        Cookie cookie =
                new Cookie(
                        "access_token",
                        "");

        
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);

        return ResponseEntity.noContent()
                .build();
    }
}