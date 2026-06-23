package com.govtech.bff.eligibility.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.govtech.bff.clients.EligibilityClient;
import com.govtech.bff.eligibility.dto.EligibilityDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EligibilityService {


    private final EligibilityClient eligibilityClient;
    
    public List<EligibilityDto> eligibilities() {
    return eligibilityClient.eligibilities();
}


    
}
