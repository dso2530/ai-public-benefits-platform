package com.govtech.bff.dashboard.controller;

import com.govtech.bff.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;

import com.govtech.bff.dashboard.dto.DashboardResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;     

    @GetMapping("/api/dashboard")
    public DashboardResponse dashboard() {
        return dashboardService.getDashboard();
    }
}