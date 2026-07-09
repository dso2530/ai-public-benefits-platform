package com.govtech.profile.api;

import com.govtech.profile.api.dto.ProfileDto;
import com.govtech.profile.application.usecase.GetProfileUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Slf4j
public class ProfileController {

  private final GetProfileUseCase getProfileUseCase;

  @GetMapping("/me")
  public ProfileDto me(@AuthenticationPrincipal Jwt jwt) {
    log.debug("Subject received={}", jwt.getSubject());
    return getProfileUseCase.getProfile(jwt.getSubject());
  }
}
