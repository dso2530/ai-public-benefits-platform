package com.govtech.eligibility.api;

import com.govtech.eligibility.api.dto.EligibilityDto;
import com.govtech.eligibility.api.dto.EligibilitySummaryDto;
import com.govtech.eligibility.application.usecase.GetEligibilityListUsecase;
import com.govtech.eligibility.application.usecase.GetEligibilitySummaryUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EligibilityController {

  private final GetEligibilitySummaryUsecase summaryService;
  private final GetEligibilityListUsecase listService;

  @GetMapping("/api/eligibility/summary")
  public EligibilitySummaryDto summary(@AuthenticationPrincipal Jwt jwt) {
    return summaryService.execute(jwt.getSubject());
  }

  @GetMapping("/api/eligibility")
  public List<EligibilityDto> eligibilities(@AuthenticationPrincipal Jwt jwt) {
    return listService.execute(jwt.getSubject());
  }
}