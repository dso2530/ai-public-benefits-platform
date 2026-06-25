package com.govtech.bff.auth.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.bff.auth.dto.InternalUser;
import com.govtech.bff.auth.dto.MeResponse;

@RestController
@RequestMapping("/api/me")
@RequiredArgsConstructor
public class MeController {

    @GetMapping
    public MeResponse me(
            @AuthenticationPrincipal InternalUser user) {

        return new MeResponse(
                user.subject(),
                user.name(),
                user.email());
    }
}