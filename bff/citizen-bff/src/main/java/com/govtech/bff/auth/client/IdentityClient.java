package com.govtech.bff.auth.client;

import com.govtech.bff.auth.dto.CreateUserRequest;
import com.govtech.bff.auth.dto.UserProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class IdentityClient {

    private final RestClient restClient;

    @Value("${services.identity.url}")
    private String identityUrl;

    public UserProfileResponse findByFranceConnectSub(
            String sub) {

        try {

            return restClient.get()
                    .uri(identityUrl +
                            "/api/users/franceconnect/" + sub)
                    .retrieve()
                    .body(UserProfileResponse.class);

        } catch (Exception e) {

            return null;
        }
    }

    public UserProfileResponse createUser(
            CreateUserRequest request) {

        return restClient.post()
                .uri(identityUrl + "/api/users")
                .body(request)
                .retrieve()
                .body(UserProfileResponse.class);
    }
}