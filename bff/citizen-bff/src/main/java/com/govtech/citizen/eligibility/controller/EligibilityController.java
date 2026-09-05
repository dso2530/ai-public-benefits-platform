package com.govtech.citizen.eligibility.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.citizen.eligibility.dto.EligibilityDto;
import com.govtech.citizen.eligibility.service.EligibilityService;

@RestController
@RequiredArgsConstructor
public class EligibilityController {

  private final EligibilityService eligibilityService;

  @GetMapping("/api/eligibility")
  public List<EligibilityDto> benefits() {
    return eligibilityService.getEligibilities();
  }
}
