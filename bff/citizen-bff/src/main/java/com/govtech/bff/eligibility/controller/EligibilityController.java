package com.govtech.bff.eligibility.controller;

import com.govtech.bff.eligibility.dto.EligibilityDto;
import com.govtech.bff.eligibility.service.EligibilityService;

import lombok.RequiredArgsConstructor;


import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EligibilityController {

    private final EligibilityService eligibilityService;     

    @GetMapping("/api/eligibility")
    public List<EligibilityDto> benefits() {
        return eligibilityService.eligibilities();
    }
}