package com.govtech.citizen.dashboard.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.govtech.citizen.dashboard.dto.DashboardResponse;
import com.govtech.citizen.dashboard.service.DashboardService;

@RestController
@RequiredArgsConstructor
public class DashboardController {

  private final DashboardService dashboardService;

  @GetMapping("/api/dashboard")
  public DashboardResponse dashboard() {
    return dashboardService.getDashboard();
  }
}
