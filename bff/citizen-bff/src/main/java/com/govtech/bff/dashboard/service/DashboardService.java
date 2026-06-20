package com.govtech.bff.dashboard.service;

import com.govtech.bff.clients.DocumentClient;
import com.govtech.bff.clients.EligibilityClient;
import com.govtech.bff.clients.NotificationClient;
import com.govtech.bff.clients.ProfileClient;
import com.govtech.bff.dashboard.dto.*;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {


      private final ProfileClient profileClient;
      private final EligibilityClient eligibilityClient;
      private final NotificationClient notificationClient;
      private final DocumentClient documentClient;      

        
      public DashboardResponse getDashboard() {

        return DashboardResponse.builder()
                .household(profileClient.getHousehold())
                .benefits(eligibilityClient.getSummary())
                .notifications(notificationClient.getSummary())
                .documents(documentClient.getSummary())
                .build();
    }   
       
    
}