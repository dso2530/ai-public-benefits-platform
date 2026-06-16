package com.govtech.bff.auth.service;

import com.govtech.bff.auth.client.IdentityClient;
import com.govtech.bff.auth.dto.CreateUserRequest;
import com.govtech.bff.auth.dto.UserProfileResponse;
import com.govtech.bff.auth.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final IdentityClient identityClient;
    private final UserMapper userMapper;

    public UserProfileResponse provisionUser(
            OidcUser oidcUser) {

        String sub = oidcUser.getSubject();

        UserProfileResponse existingUser =
                identityClient.findByFranceConnectSub(sub);

        if (existingUser != null) {

           
            return existingUser;
        }

        

        CreateUserRequest request =
                userMapper.toCreateUserRequest(
                        oidcUser);

        return identityClient.createUser(
                request);
    }
}