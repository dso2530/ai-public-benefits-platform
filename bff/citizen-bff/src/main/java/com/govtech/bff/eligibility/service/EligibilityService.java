package com.govtech.bff.eligibility.service;

import com.govtech.bff.clients.EligibilityClient;
import com.govtech.bff.eligibility.dto.EligibilityDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EligibilityService {

  private final EligibilityClient eligibilityClient;

  public List<EligibilityDto> eligibilities() {
    return eligibilityClient.eligibilities();
  }
}
