package com.govtech.bff.application;

import com.govtech.bff.dto.*;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {


    public DashboardResponse getDashboard() {

        HouseholdDto household =
                new HouseholdDto(
                        "Roubaix",
                        "TENANT",
                        2,
                        true
                );

        BenefitsDto benefits =
                new BenefitsDto(
                        7,
                        2,
                        4280
                );

        NotificationsDto notifications =
                new NotificationsDto(
                        3
                );

        return new DashboardResponse(
                household,
                benefits,
                notifications
        );
    }
}